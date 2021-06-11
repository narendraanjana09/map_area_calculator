package com.nsa.mapsspeech.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
import com.jroviraa.detailtextview.DetailTextView;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.ProgressBar;
import com.nsa.mapsspeech.Model.CropModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import static com.nsa.mapsspeech.Activities.AddOnMap.fUser;
import static com.nsa.mapsspeech.Activities.AddOnMap.getAreaBigha;
import static com.nsa.mapsspeech.Activities.AddOnMap.getMonthName;


public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.MyViewHolder> {


    Context context;
    List<WorkModel> workModelList;

    public WorkAdapter(Context context, List<WorkModel> workModelList) {
        this.context = context;
        this.workModelList = workModelList;
    }

    @NonNull
    @NotNull
    @Override
    public WorkAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.work_recycler_row,parent,false);
        return new WorkAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WorkAdapter.MyViewHolder holder, int position) {
        WorkModel model=workModelList.get(position);

        holder.serialNumberTv.setText(position+1+".");
        holder.dateTv.setText(model.getDate());
        holder.labourCountTV.setText(model.getLabourCount());
        holder.costTV.setText(model.getCost());
        holder.labourNamesTV.setText(model.getLabourNames());
        holder.workDesTV.setText(model.getWorkDescription());



    }

    @Override
    public int getItemCount() {
        return workModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv,costTV,labourCountTV,serialNumberTv;
        TextView labourNamesTV,workDesTV;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            serialNumberTv=itemView.findViewById(R.id.serialNumberTV);
            dateTv=itemView.findViewById(R.id.workDateTextView);
            costTV=itemView.findViewById(R.id.costTextView);
            labourCountTV=itemView.findViewById(R.id.labourCountTextView);
            labourNamesTV=itemView.findViewById(R.id.labourNamesTextView);
            workDesTV=itemView.findViewById(R.id.workDesTextView);

        }
    }
}
