package com.nsa.mapsspeech.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.PolyUtil;
import com.maxkeppeler.sheets.input.InputSheet;
import com.maxkeppeler.sheets.input.type.InputEditText;
import com.maxkeppeler.sheets.input.type.InputSeparator;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.ProgressBar;
import com.nsa.mapsspeech.ExtraClasses.SwipeToDeleteCallback;
import com.nsa.mapsspeech.Model.CropModel;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static com.nsa.mapsspeech.Activities.AddOnMap.fUser;
import static com.nsa.mapsspeech.Activities.AddOnMap.getAreaBigha;
import static com.nsa.mapsspeech.Activities.AddOnMap.getMonthName;


public class ViewCropsAdapter extends RecyclerView.Adapter<ViewCropsAdapter.MyViewHolder> {


    Context context;
    List<CropModel> cropModelList;
    DatabaseReference workReference;
    public ViewCropsAdapter(Context context, List<CropModel> cropModelList) {
        this.context = context;
        this.cropModelList = cropModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewCropsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.field_recycler_row,parent,false);
        return new ViewCropsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        CropModel model=cropModelList.get(position);
        workReference=  new FireBase().getReferenceCrops()
                .child(fUser.getUid()).child(model.getKey()).child("work");
        holder.fieldNameTV.setText(position+1+".  "+model.getFieldName());
        holder.fieldAreaTV.setText(getAreaBigha(PolyUtil.decode(model.getLatlngList()))+" Bigha");
        holder.seedSpecsTV.setText(model.getCropSpecification());
        holder.fieldPlantedDateTV.setText(model.getCropPlantingDate());
        holder.fabAddWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWork(model.getKey());
            }
        });

        holder.fabMoreLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.hiddenView.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.GONE);
                    holder.fabMoreLess.setImageResource(R.drawable.more);
                }
                else {

                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.VISIBLE);
                    holder.fabMoreLess.setImageResource(R.drawable.less);
                }
            }
        });



        FirebaseRecyclerOptions<WorkModel> options
                = new FirebaseRecyclerOptions.Builder<WorkModel>()
                .setQuery(workReference, WorkModel.class)
                .build();

        WorkOptionsAdapter adapter = new WorkOptionsAdapter(options,context);

        holder.fabShowWorkRV.setVisibility(View.VISIBLE);

        adapter.startListening();
        enableSwipeToDeleteAndUndo(holder,adapter,workReference);

        holder.workrecyclerView.setAdapter(adapter);

        holder.fabShowWorkRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.workrecyclerView.getVisibility() == View.VISIBLE) {


                    TransitionManager.beginDelayedTransition(holder.workrecyclerView,
                            new AutoTransition());
                    holder.workrecyclerView.setVisibility(View.GONE);
                    holder.fabShowWorkRV.setImageResource(R.drawable.more);
                }
                else {

                    if(options.getSnapshots().size()==0){
                        Toast.makeText(context, "No Work Uploaded", Toast.LENGTH_SHORT).show();
                    }
                    TransitionManager.beginDelayedTransition(holder.workrecyclerView,
                            new AutoTransition());
                    holder.workrecyclerView.setVisibility(View.VISIBLE);
                    holder.fabShowWorkRV.setImageResource(R.drawable.less);
                }
            }
        });

//        List<WorkModel> workList=new ArrayList<>();
//        new FireBase().getReferenceCrops()
//                .child(fUser.getUid()).child(model.getKey()).child("work").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot snapshot1:snapshot.getChildren()){
//                        WorkModel model1=snapshot1.getValue(WorkModel.class);
//                        workList.add(model1);
//                    }
//                    holder.fabShowWorkRV.setVisibility(View.VISIBLE);
//                    WorkAdapter adapter=new WorkAdapter(context,workList);
//                    holder.workrecyclerView.setAdapter(adapter);
//                }else{
//                    Toast.makeText(context, "No Work", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Toast.makeText(context, "error Work", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void enableSwipeToDeleteAndUndo(MyViewHolder holder, WorkOptionsAdapter mAdapter
            , DatabaseReference workReference) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final WorkModel item = mAdapter.getItem(position);

                workReference.child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar snackbar = Snackbar
                                .make(holder.cardView, "Item removed from the list.", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.BLACK);
                        snackbar.show();
                        mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                      mAdapter.getSnapshots().add(position,item);
                        Snackbar snackbar = Snackbar
                                .make(holder.cardView, "Item removed failed.", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.BLACK);
                        snackbar.show();
                        snackbar.show();
                    }
                });


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(holder.workrecyclerView);
    }

    private void getWork(String key) {
        new InputSheet().show(context, LinearLayout.LayoutParams.MATCH_PARENT, new Function1<InputSheet, Unit>() {
            @Override
            public Unit invoke(InputSheet inputSheet) {
                inputSheet.cornerRadius(10f);
                inputSheet.cancelableOutside(false);
                inputSheet.setCancelable(false);
                inputSheet.onPositive("Pick Date",new Function1<Bundle, Unit>() {
                    @Override
                    public Unit invoke(Bundle bundle) {
                         String labourCount=bundle.getString("labourCount");
                        String workDes=bundle.getString("workDes");
                        String labourNames=bundle.getString("laboursName");
                        String cost=bundle.getString("cost");
                        if(labourNames==null){
                            labourNames="null";
                        }
                        WorkModel model=new WorkModel("",labourCount,labourNames,workDes,cost,"");
                        getDate(model,"Pick A Date",key);
                        return null;
                    }
                });
                inputSheet.onNegative(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        return null;
                    }
                });

                return null;
            }
        }).with(new InputSeparator("separator", new Function1<InputSeparator, Unit>() {
            @Override
            public Unit invoke(InputSeparator inputSeparator) {
                inputSeparator.label("Work Info  👇");
                inputSeparator.displayDivider(true);

                return null;
            }
        }),new InputEditText("labourCount", new Function1<InputEditText, Unit>() {
                    @Override
                    public Unit invoke(InputEditText inputEditText) {
                        inputEditText.hint("Type Here");
                        inputEditText.label("Labours Count");
                        inputEditText.startIconDrawable(R.drawable.counting_people);
                        inputEditText.inputType(InputType.TYPE_CLASS_NUMBER);
                        inputEditText.required(true);

                        return null;
                    }
                }),new InputEditText("laboursName", new Function1<InputEditText, Unit>() {
            @Override
            public Unit invoke(InputEditText inputEditText) {
                inputEditText.hint("Type Here");
                inputEditText.startIconDrawable(R.drawable.name_tag);
                inputEditText.label("Labours Name");
                inputEditText.inputType(InputType.TYPE_CLASS_TEXT);
                return null;
            }
        }),new InputEditText("workDes", new Function1<InputEditText, Unit>() {
            @Override
            public Unit invoke(InputEditText inputEditText) {
                inputEditText.hint("Type Here");
                inputEditText.startIconDrawable(R.drawable.text_box);
                inputEditText.label("Work Description");
                inputEditText.inputType(InputType.TYPE_CLASS_TEXT);
                inputEditText.required(true);
                return null;
            }
        }),new InputEditText("cost", new Function1<InputEditText, Unit>() {
            @Override
            public Unit invoke(InputEditText inputEditText) {
                inputEditText.hint("Type Here");
                inputEditText.label("Cost");
                inputEditText.startIconDrawable(R.drawable.rupee);
                inputEditText.inputType(InputType.TYPE_CLASS_NUMBER);
                inputEditText.required(true);
                return null;
            }
        }));
    }


    private void uploadWorkToFirebase(WorkModel model, String key) {
       if(fUser.getUid()==null){
           Toast.makeText(context, "id null try later", Toast.LENGTH_SHORT).show();
           return;
       }
        ProgressBar progressBar=new ProgressBar(context,"Uploading...");
        progressBar.show();

        String dt =new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date());
        model.setKey(dt);
        new FireBase().getReferenceCrops()
                .child(fUser.getUid()).child(key).child("work").child(dt).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                progressBar.hide();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
                progressBar.hide();

            }
        });
    }


    String date="";
    String year="";
    private void getDate(WorkModel model, String message, String key) {
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
                              getDate(model,"Year Must Be Current Year", key);
                          }else {

                              date=getMonthName(monthOfYear + 1) + " " + dayOfMonth + ", " + year;
                              model.setDate(date);
                              uploadWorkToFirebase(model,key);

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
        return cropModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fieldNameTV,fieldPlantedDateTV,fieldAreaTV,seedSpecsTV;
        private FloatingActionButton fabMoreLess,fabAddWork,fabShowWorkRV;
        LinearLayout hiddenView;
        CardView cardView;
        RecyclerView workrecyclerView;


        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            hiddenView=itemView.findViewById(R.id.hidden_view);
            cardView=itemView.findViewById(R.id.base_cardview);



            fieldNameTV=itemView.findViewById(R.id.fieldNameTextView);
            fieldAreaTV=itemView.findViewById(R.id.fieldAreaTextView);
            fieldPlantedDateTV=itemView.findViewById(R.id.plantedDateTV);
            fabMoreLess=itemView.findViewById(R.id.fabMoreButton);
            fabShowWorkRV=itemView.findViewById(R.id.fabShowWRV);
            fabAddWork=itemView.findViewById(R.id.fabAddWork);
            seedSpecsTV=itemView.findViewById(R.id.seedSpecsTextView);
            workrecyclerView=itemView.findViewById(R.id.workRecyclerView);

        }
    }
}
