package com.example.myapplicationfirebase.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplicationfirebase.Editar;
import com.example.myapplicationfirebase.POJO.PlanE;

import java.util.ArrayList;

import com.example.myapplicationfirebase.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlanesAdaptador extends RecyclerView.Adapter<PlanesAdaptador.PlanViewHolder> {
    ArrayList<PlanE> listaContactos;
    Activity activity;
    ArrayList<String> listaKEys;
    private DatabaseReference mDatabase;

    public PlanesAdaptador(ArrayList<PlanE> listaContactos, Activity activity, ArrayList<String> listaKeys) {
        this.listaContactos = listaContactos;
        this.activity = activity;
        this.listaKEys = listaKeys;
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_planestudios, parent, false);
        return new PlanesAdaptador.PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        final PlanE plan = listaContactos.get(position);
        final String keyPlan = listaKEys.get(position);
        holder.nombrePlan.setText(plan.getNombre());

        holder.clavePlan.setText("Nombre: " + plan.getNombre() + " " + plan.getApellidos());
        holder.nombrePlan.setText("Edad: " + plan.getEdad());


        holder.nombrePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pl = new Intent(activity, Editar.class);
                pl.putExtra("keyplan", keyPlan);
                pl.putExtra("nombre", plan.getNombre());
                pl.putExtra("edad", plan.getEdad());
                pl.putExtra("apellidos", plan.getApellidos());
                activity.startActivity(pl);
            }
        });
        holder.clavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pl = new Intent(activity, Editar.class);
                pl.putExtra("keyplan", keyPlan);
                pl.putExtra("nombre", plan.getNombre());
                pl.putExtra("edad", plan.getEdad());
                pl.putExtra("apellidos", plan.getApellidos());
                activity.startActivity(pl);
            }
        });

        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("¿Desea eliminar el elemento?")
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarNodo(keyPlan);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }


    public void eliminarNodo(final String keyNodoAEliminar) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabase.child("personas").child(keyNodoAEliminar).removeValue();


                //InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                //im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Snackbar.make(activity.getCurrentFocus(), "Se ha eliminado correctamente !!!", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Snackbar.make(activity.getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }


    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private TextView nombrePlan;
        private CardView miCard;
        private TextView clavePlan;
        private ImageButton imgBtnDelete;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrePlan = (TextView) itemView.findViewById(R.id.tvt_nombre);
            clavePlan = (TextView) itemView.findViewById(R.id.tvt_clavePlan);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnEliminarNodo);

        }

    }


}
