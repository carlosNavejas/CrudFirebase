package com.example.myapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.example.myapplicationfirebase.POJO.PlanE;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Editar extends AppCompatActivity {


    private EditText eddnombre, eddapellidos, eddedad;

    private String nombre, apellidos;
    private int edad;
    private String keyPlan;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        inicarComponentes();
        llenarCamposAEditar();


    }

    public void guardarEdicion(final View view) {
        if (validarCampos()) {
            final PlanE planEdicion = new PlanE();
            planEdicion.setNombre(eddnombre.getText().toString());
            planEdicion.setApellidos(eddapellidos.getText().toString());
            planEdicion.setEdad(Integer.parseInt(eddedad.getText().toString()));
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mDatabase.child("personas").child(keyPlan).setValue(planEdicion);
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    limpiarCampos();
                    Snackbar.make(getCurrentFocus(), "Editado con exito!!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Snackbar.make(getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void limpiarCampos() {
        eddedad.setText(null);
        eddapellidos.setText(null);
        eddnombre.setText(null);
    }

    public void inicarComponentes() {
        nombre = getIntent().getExtras().getString("nombre");
        apellidos = getIntent().getExtras().getString("apellidos");
        edad = getIntent().getExtras().getInt("edadd");
        keyPlan = getIntent().getExtras().getString("keyplan");
        eddnombre = findViewById(R.id.edt_nombreEditar);
        eddapellidos = findViewById(R.id.edt_apellidosEditar);
        eddedad = findViewById(R.id.edt_edadEditar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void llenarCamposAEditar() {
        eddnombre.setText(nombre);
        eddapellidos.setText(apellidos);
        eddedad.setText(edad + "");
    }

    public boolean validarCampos() {
        if (eddnombre.getText().toString().isEmpty()) {
            eddnombre.setError("Requerido");
            return false;
        }
        if (eddapellidos.getText().toString().isEmpty()) {
            eddapellidos.setError("Requerido");
            return false;
        }
        if (eddedad.getText().toString().isEmpty()) {
            eddedad.setError("Requerido");
            return false;
        }
        return true;
    }
}
