package com.nsa.mapsspeech.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.nsa.mapsspeech.Activities.AddOnMap;
import com.nsa.mapsspeech.Activities.MapsActivity;
import com.nsa.mapsspeech.Activities.ViewListActivity;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.SwipeToDeleteCallback;
import com.nsa.mapsspeech.Model.PlaceModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.nsa.mapsspeech.Activities.MapsActivity.getRoundValue;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    Context context;
    List<RouteModel> routeAreaModelList;
    List<PlaceModel> placeModelList;
    boolean isRoute;



    public ListViewAdapter(Context context, List<RouteModel> routeAreaModelList, boolean isRoute) {
        this.context = context;
        this.routeAreaModelList = routeAreaModelList;
        this.isRoute = isRoute;



    }

    public ListViewAdapter(Context context, List<PlaceModel> placeModelList) {
        this.context = context;
        this.placeModelList = placeModelList;


    }

    @NonNull
    @NotNull
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.list_recycler_row,parent,false);
        return new ListViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(routeAreaModelList!=null){
            RouteModel model=routeAreaModelList.get(position);
            List<LatLng> latLngList= PolyUtil.decode(model.getPoints());
            if(isRoute){
                Toast.makeText(context, "routelist", Toast.LENGTH_SHORT).show();
                double computerLength= SphericalUtil.computeLength(latLngList);
                double cl= getRoundValue(computerLength);
                holder.infoTV.setText(cl+" meter");
            }else{
                Toast.makeText(context, "arealist", Toast.LENGTH_SHORT).show();
                double computerLength= SphericalUtil.computeArea(latLngList);
                double cl= getRoundValue(computerLength);
                holder.infoTV.setText(cl+" meter/sq");
            }
            holder.indexTV.setText(position+1+". "+model.getName());

        }else{
            holder.infoTV.setVisibility(View.GONE);
            Toast.makeText(context, "placemodel", Toast.LENGTH_SHORT).show();
            PlaceModel model=placeModelList.get(position);
            holder.indexTV.setText(position+1+". "+model.getPlaceName());

        }


    }




    @Override
    public int getItemCount() {

        if(routeAreaModelList==null){
            return placeModelList.size();
        }else{
            return routeAreaModelList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView indexTV,infoTV;
        CardView mainLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            indexTV=itemView.findViewById(R.id.indexTV);
            infoTV=itemView.findViewById(R.id.infoTV);
            mainLayout=itemView.findViewById(R.id.parent_layout);
        }
    }
}
