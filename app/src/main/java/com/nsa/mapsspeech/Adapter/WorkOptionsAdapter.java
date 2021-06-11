package com.nsa.mapsspeech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import static com.nsa.mapsspeech.Activities.AddOnMap.fUser;

public class WorkOptionsAdapter extends FirebaseRecyclerAdapter<
        WorkModel, WorkOptionsAdapter.personsViewholder> {

   Context context;

    public WorkOptionsAdapter(
            @NonNull FirebaseRecyclerOptions<WorkModel> options, Context context)
    {

        super(options);
        this.context=context;

    }
    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull personsViewholder holder,
                     int position, @NonNull WorkModel model)
    {

        holder.serialNumberTv.setText(position+1+".");
        holder.dateTv.setText(model.getDate());
        holder.labourCountTV.setText(model.getLabourCount());
        holder.costTV.setText(model.getCost());
        holder.labourNamesTV.setText(model.getLabourNames());
        holder.workDesTV.setText(model.getWorkDescription());
    }


    @NonNull
    @Override
    public personsViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_recycler_row, parent, false);
        return new WorkOptionsAdapter.personsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView dateTv,costTV,labourCountTV,serialNumberTv;
        TextView labourNamesTV,workDesTV;

        public personsViewholder(@NonNull View itemView)
        {
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

