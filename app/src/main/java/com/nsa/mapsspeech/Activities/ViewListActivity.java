package com.nsa.mapsspeech.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.maxkeppeler.sheets.input.InputSheet;
import com.maxkeppeler.sheets.input.type.InputEditText;
import com.maxkeppeler.sheets.input.type.InputSeparator;
import com.nsa.mapsspeech.Adapter.ListViewAdapter;
import com.nsa.mapsspeech.Adapter.OWAdapter;
import com.nsa.mapsspeech.Adapter.ViewCropsAdapter;
import com.nsa.mapsspeech.Adapter.WorkOptionsAdapter;
import com.nsa.mapsspeech.ExtraClasses.FireBase;
import com.nsa.mapsspeech.ExtraClasses.SwipeToDeleteCallback;
import com.nsa.mapsspeech.Model.PlaceModel;
import com.nsa.mapsspeech.Model.RouteModel;
import com.nsa.mapsspeech.Model.Work.OWModel;
import com.nsa.mapsspeech.Model.Work.WorkModel;
import com.nsa.mapsspeech.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import static com.nsa.mapsspeech.Activities.AddOnMap.areaModelList;
import static com.nsa.mapsspeech.Activities.AddOnMap.fUser;
import static com.nsa.mapsspeech.Activities.AddOnMap.getDayMonthYear;
import static com.nsa.mapsspeech.Activities.AddOnMap.placeModelList;
import static com.nsa.mapsspeech.Activities.AddOnMap.routeModelList;

public class ViewListActivity extends AppCompatActivity {

    private String task="";
    static RecyclerView listRV;
    ListViewAdapter viewAdapter;
    ConstraintLayout mainLayout;
    DatabaseReference reference;
    ExtendedFloatingActionButton fabDeleteAll,fabAddWork;
    List<OWModel> workModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        listRV=findViewById(R.id.listViewRV);
        mainLayout=findViewById(R.id.mainLayout);
        fabDeleteAll=findViewById(R.id.deleteAllFab);
        fabAddWork=findViewById(R.id.addWorkFAB);


        Bundle bundle=getIntent().getExtras();
        ActionBar actionBar=getSupportActionBar();

        if(bundle!=null){
             String query=bundle.getString("query");
           if(query.equals("places")){
               task="places";
               reference=new FireBase().getReferenceDataPlace();
               actionBar.setTitle("Configure Uploaded Places");
               viewAdapter=new ListViewAdapter(getApplicationContext(),placeModelList);
               listRV.setAdapter(viewAdapter);
           }else if(query.equals("routes")){
               reference=new FireBase().getReferenceDataPolyLine();
               viewAdapter=new ListViewAdapter(getApplicationContext(),routeModelList,true);
               actionBar.setTitle("Configure Uploaded Routes");
               listRV.setAdapter(viewAdapter);
               task="routes";
           }else if(query.equals("areas")){
               reference=new FireBase().getReferenceDataArea();
               viewAdapter=new ListViewAdapter(getApplicationContext(),areaModelList,false);
               actionBar.setTitle("Configure Uploaded Areas");
               listRV.setAdapter(viewAdapter);
               task="areas";
           }else if(query.equals("work")){
               reference=new FireBase().getReferenceWork();
               workModelList=new ArrayList<>();
               actionBar.setTitle("Your Work");
               fabDeleteAll.setVisibility(View.GONE);
               fabAddWork.setVisibility(View.VISIBLE);

           }
           reference=reference.child(fUser.getUid());
           if(query.equals("work")){
               getWorkList();
           }
        }
        Snackbar snackbar
                = Snackbar
                .make(mainLayout, "swipe from right to left to delete an item", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.BLACK);
        snackbar.setDuration(6000);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
         enableSwipeToDeleteAndUndo();
    }
       OWAdapter workAdapter;
    private void getWorkList() {
        FirebaseRecyclerOptions<OWModel> options
                = new FirebaseRecyclerOptions.Builder<OWModel>()
                .setQuery(reference, OWModel.class)
                .build();
        workAdapter=new OWAdapter(options,ViewListActivity.this);
        workAdapter.startListening();
        listRV.setAdapter(workAdapter);
        enableSwipeToDeleteAndUndo(workAdapter,reference);

    }
    private void enableSwipeToDeleteAndUndo(OWAdapter mAdapter
            , DatabaseReference workReference) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final OWModel item = mAdapter.getItem(position);

                workReference.child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar snackbar = Snackbar
                                .make(mainLayout, "Item removed from the list.", Snackbar.LENGTH_LONG);
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
                                .make(mainLayout, "Item removed failed.", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.BLACK);
                        snackbar.show();
                        snackbar.show();
                    }
                });


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(listRV);
    }


    private String TAG="listAct";
    public void deleteList(View view) {
        if(task.contains("place")){
            new FireBase().getReferenceDataPlace().child(fUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    Log.e(TAG,"places deleted");
                    if(placeModelList!=null){
                        placeModelList.clear();
                    }
                }
            });

        }else if(task.contains("area")) {
            new FireBase().getReferenceDataArea().child(fUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    Log.e(TAG,"routes deleted");
                    if(areaModelList!=null){
                        areaModelList.clear();
                    }

                }
            });

        }else if(task.contains("routes")){
            new FireBase().getReferenceDataPolyLine().child(fUser.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    Log.e(TAG,"routes deleted");
                    if(routeModelList!=null){
                        routeModelList.clear();

                    }

                }
            });
        }
    finish();
    }
    PlaceModel placeModel;
    RouteModel routeModel;
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                String key="";

                if(task.contains("place")){
                    placeModel=placeModelList.get(position);
                    key=placeModel.getKey();
                }else if(task.contains("area")) {
                routeModel=areaModelList.get(position);
                key=routeModel.getKey();
                }else if(task.contains("routes")){
                    routeModel=routeModelList.get(position);
                    key=routeModel.getKey();
                }



                reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar snackbar = Snackbar
                                .make(mainLayout, "Item removed from the list.", Snackbar.LENGTH_LONG);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.BLACK);
                        snackbar.show();
                        int size=0;
                        if(task.contains("place")){
                            placeModelList.remove(position);
                            size=placeModelList.size();
                            viewAdapter.notifyItemRemoved(position);
                        }else if(task.contains("area")) {
                            areaModelList.remove(position);
                            size=areaModelList.size();
                            viewAdapter.notifyItemRemoved(position);
                        }else if(task.contains("routes")){
                            routeModelList.remove(position);
                            size=routeModelList.size();
                            viewAdapter.notifyItemRemoved(position);

                        }
                        viewAdapter.notifyItemRangeChanged(0,viewAdapter.getItemCount());
                        if(size==0){
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Snackbar snackbar = Snackbar
                                .make(mainLayout, "Item removed failed.", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                });


            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(listRV);
    }

    public void addWork(View view) {
        new InputSheet().show(ViewListActivity.this, LinearLayout.LayoutParams.MATCH_PARENT, new Function1<InputSheet, Unit>() {
            @Override
            public Unit invoke(InputSheet inputSheet) {
                inputSheet.cornerRadius(10f);
                inputSheet.cancelableOutside(false);
                inputSheet.setCancelable(false);
                inputSheet.onPositive("Upload",new Function1<Bundle, Unit>() {
                    @Override
                    public Unit invoke(Bundle bundle) {

                        String date=getDayMonthYear()+" "+new SimpleDateFormat("hh:mm a").format(new Date());
                        String key=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String workDes=bundle.getString("workDes");
                        OWModel model=new OWModel(key,workDes,date);
                        uploadWorkToFirebase(model);

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
                inputSeparator.label("Work Info👇");
                inputSeparator.drawable(R.drawable.fieldandtractor);
                inputSeparator.displayDivider(true);

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
        }));
    }

    private void uploadWorkToFirebase(OWModel model) {
        reference.child(model.getKey()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ViewListActivity.this, "Work Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}