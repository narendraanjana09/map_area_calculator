package com.nsa.mapsspeech.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.PolyUtil;
import com.nsa.mapsspeech.Activities.AddOnMap;
import com.nsa.mapsspeech.Activities.CropsActivity;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.ProgressBar;
import com.nsa.mapsspeech.Model.CropModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nsa.mapsspeech.Activities.AddOnMap.cropList;
import static com.nsa.mapsspeech.Activities.AddOnMap.fUser;
import static com.nsa.mapsspeech.Activities.AddOnMap.getAreaBigha;
import static com.nsa.mapsspeech.Activities.AddOnMap.getMonthName;


public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.MyViewHolder> {


    Context context;
    List<RouteModel> areaModelList;

    public CropsAdapter(Context context, List<RouteModel> areaModelList) {
        this.context = context;
        this.areaModelList = areaModelList;
    }

    @NonNull
    @NotNull
    @Override
    public CropsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.crops_recycler_row,parent,false);
        return new CropsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CropsAdapter.MyViewHolder holder, int position) {

        RouteModel model=areaModelList.get(position);
        String fieldName=model.getName();
        holder.fieldNameTV.setText(position+1+".  "+fieldName);
        holder.fieldUploadedDateTV.setText(model.getDate());
        String areaPoints=model.getPoints();
        holder.fieldAreaTV.setText(getAreaBigha(PolyUtil.decode(areaPoints))+" Bigha");

        holder.fabMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.hiddenView.getVisibility() == View.VISIBLE) {

                    // The transition of the hiddenView is carried out
                    //  by the TransitionManager class.
                    // Here we use an object of the AutoTransition
                    // Class to create a default transition.
                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.GONE);
                   holder.fabMoreLess.setImageResource(R.drawable.more);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.VISIBLE);
                    holder.fabMoreLess.setImageResource(R.drawable.less);
                }
            }
        });


        holder.fabAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(holder,"Pick A Date");
            }
        });
        getCropChipData(holder);

        holder.fabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropSpecifiaction=holder.cropSpecificationET.getText().toString();
                if(cropSpecifiaction.isEmpty()){
                    Toast.makeText(context, "Please Give A Crop Specification or\nSome thing You Want To get Rememberes!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cropType.isEmpty()){
                    Toast.makeText(context, "Please select a crop type!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date.isEmpty()){
                    Toast.makeText(context, "Please select a planting date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                CropModel cropModel=new CropModel(model.getKey(),fieldName,cropType,cropSpecifiaction,areaPoints,date);
                askForUpload(cropModel,position,holder);

            }
        });
        holder.cropSpecificationET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_NONE){
                    cropSpecifiaction=holder.cropSpecificationET.getText().toString();
                }

                return true;
            }
        });
    }

    private void askForUpload(CropModel cropModel, int position, MyViewHolder holder) {
        AlertDialog dialog = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Upload Status!")
                .setCancelable(false)
                .setMessage("after uploading, you won't be able to\nedit it until the new season")
                .setPositiveButton("upload", (dialogInterface, i) -> {
                    uploadCropToFirebase(cropModel,position,holder);

                })
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .create();
        dialog.show();
    }

    private void uploadCropToFirebase(CropModel cropModel, int position, MyViewHolder holder) {
       if(fUser.getUid()==null){
           Toast.makeText(context, "id null try later", Toast.LENGTH_SHORT).show();
           return;
       }
        ProgressBar progressBar=new ProgressBar(context,"Uploading...");
        progressBar.show();


        new FireBase().getReferenceCrops()
                .child(fUser.getUid()).child(cropModel.getKey()).setValue(cropModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                areaModelList.remove(position);
                cropList.add(cropModel);
                CropsAdapter.this.notifyItemRemoved(position);
                CropsAdapter.this.notifyDataSetChanged();
                progressBar.hide();

                date="";
                cropType="";

                holder.chipRB.setActivated(false);
                holder.dateRB.setActivated(false);
                holder.dateTV.setText("");
                holder.cropSpecificationET.setText("");

                cropSpecifiaction="";
                if(CropsAdapter.this.getItemCount()==0){
                    new CropsActivity().finishOnClick();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
                progressBar.hide();

            }
        });
    }

    String cropSpecifiaction="";
         String cropType="";
    private void getCropChipData(MyViewHolder holder) {
        holder.crop_type.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null) {

                    holder.chipRB.setActivated(true);

                    cropType = chip.getText().toString();
                    if(holder.dateRB.isActivated()){
                        holder.fabUpload.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    holder.fabUpload.setVisibility(View.GONE);
                    cropType=null;
                }
            }
        });
    }

    String date="";
    String year="";
    private void getDate(@NotNull MyViewHolder holder,String message) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR),
        mMonth = c.get(Calendar.MONTH),
        mDay = c.get(Calendar.DAY_OF_MONTH);
        year=mYear+"";


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.e("date",System.currentTimeMillis()+"");
                          if(year!=mYear){
                              getDate(holder,"Year Must Be Current Year");
                          }else {
                              holder.dateRB.setActivated(true);
                              if( holder.chipRB.isActivated()){
                                  holder.fabUpload.setVisibility(View.VISIBLE);
                              }
                              date=getMonthName(monthOfYear + 1) + " " + dayOfMonth + ", " + year;
                              holder.dateTV.setText(date);
                          }
                    }
                }, mYear, mMonth, mDay);
        DatePicker datePicker=datePickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());
        datePickerDialog.setTitle(message);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return areaModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fieldNameTV,fieldUploadedDateTV,fieldAreaTV,dateTV;
        private FloatingActionButton fabMoreLess,fabAddDate;
        ExtendedFloatingActionButton fabUpload;
        LinearLayout hiddenView;
        CardView cardView;
        ChipGroup crop_type;
        RadioButton chipRB,dateRB;
        EditText cropSpecificationET;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            hiddenView=itemView.findViewById(R.id.hidden_view);
            cardView=itemView.findViewById(R.id.base_cardview);

            crop_type=itemView.findViewById(R.id.crop_type_group);
            chipRB=itemView.findViewById(R.id.chipRadioButton);
            dateRB=itemView.findViewById(R.id.dateRadioButton);
            cropSpecificationET=itemView.findViewById(R.id.cropSpecificationET);

            fieldNameTV=itemView.findViewById(R.id.fieldNameTextView);
            fieldAreaTV=itemView.findViewById(R.id.fieldAreaTextView);
            fieldUploadedDateTV=itemView.findViewById(R.id.uploadDateTextView);
            fabMoreLess=itemView.findViewById(R.id.fabMoreButton);
            fabAddDate=itemView.findViewById(R.id.fabAddDate);
            dateTV=itemView.findViewById(R.id.dateTextView);
            fabUpload=itemView.findViewById(R.id.fabUpload);
        }
    }
}
