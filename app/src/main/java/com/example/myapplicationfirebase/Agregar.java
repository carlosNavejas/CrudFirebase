package com.example.myapplicationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import java.util.UUID;


public class Agregar extends AppCompatActivity {


    private EditText nombre, apellidos, edad;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        initVariables();

    }


    public void guardarPlan(final View view) {
        if (validarCamposVacios()) {
            final PlanE p = new PlanE();


            p.setNombre(nombre.getText().toString());
            p.setApellidos(apellidos.getText().toString());


            p.setEdad(Integer.parseInt(edad.getText().toString()));


            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String clavePojo = UUID.randomUUID().toString();

                    mDatabase.child("personas").child(clavePojo).setValue(p);

                    limpiarCampos();
                    InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Snackbar.make(getCurrentFocus(), "Registrado con exito!!", Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Snackbar.make(getCurrentFocus(), "Ha ocurrido un error\nIntentelo mas tarde", Snackbar.LENGTH_SHORT).show();
                }
            });

        }
    }

    public Boolean validarCamposVacios() {
        if (nombre.getText().toString().isEmpty()) {
            nombre.setError("Requerido");
            return false;
        }

        if (apellidos.getText().toString().isEmpty()) {
            apellidos.setError("Requerido");
            return false;
        }
        if (edad.getText().toString().isEmpty()) {
            edad.setError("Requerido");
            return false;
        }

        return true;
    }

    public void initVariables() {
        nombre = findViewById(R.id.edtNombreAgregar);
        apellidos = findViewById(R.id.edtApellidosAgregar);
        edad = findViewById(R.id.edtEdadAgregar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void limpiarCampos() {
        nombre.setText(null);
        apellidos.setText(null);
        edad.setText(null);
    }
}
