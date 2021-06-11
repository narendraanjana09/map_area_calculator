package com.nsa.mapsspeech.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nsa.mapsspeech.Model.Work.OWModel;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

public class OWAdapter extends FirebaseRecyclerAdapter<
        OWModel, OWAdapter.personsViewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
      Context context;
    public OWAdapter(@NonNull @NotNull FirebaseRecyclerOptions<OWModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull personsViewholder holder, int position, @NonNull @NotNull OWModel model) {
        holder.indexTV.setText(position+1+". "+model.getWorkDes());
        holder.infoTV.setText(model.getDate());
    }

    @NonNull
    @NotNull
    @Override
    public personsViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recycler_row, parent, false);
        return new OWAdapter.personsViewholder(view);
    }

    public class personsViewholder extends RecyclerView.ViewHolder {
        TextView indexTV,infoTV;
        CardView mainLayout;
        public personsViewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            indexTV=itemView.findViewById(R.id.indexTV);
            infoTV=itemView.findViewById(R.id.infoTV);
            mainLayout=itemView.findViewById(R.id.parent_layout);
        }
    }
}
