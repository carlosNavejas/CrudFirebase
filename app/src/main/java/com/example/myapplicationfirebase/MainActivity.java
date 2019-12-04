package com.example.myapplicationfirebase;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplicationfirebase.POJO.PlanE;
import com.example.myapplicationfirebase.RecyclerView.PlanesAdaptador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PlanE> lista_planes;
    private ArrayList<String> listaKeys;
    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inicialrComponentes();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Agregar.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void iniciarAdaptador() {

        PlanesAdaptador adaptador = new PlanesAdaptador(lista_planes, MainActivity.this, listaKeys);
        recyclerView.setAdapter(adaptador);

    }

    public void iniciarLista() {
        databaseReference.child("planesdeestudio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lista_planes.clear();
                listaKeys.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    PlanE c = d.getValue(PlanE.class);
                    lista_planes.add(c);
                    listaKeys.add(d.getKey().toString());
                }
                iniciarAdaptador();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Error al cargar la base de datos: " + databaseError.toException(), Snackbar.LENGTH_SHORT).show();
            }
        });


    }


    public void inicialrComponentes() {
        FirebaseApp.initializeApp(MainActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.miRecicleyView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        lista_planes = new ArrayList<>();
        listaKeys = new ArrayList<>();
        iniciarLista();
    }

}
