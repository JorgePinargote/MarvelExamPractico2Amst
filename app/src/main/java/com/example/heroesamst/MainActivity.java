package com.example.heroesamst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText sBar;
    private Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sBar = findViewById(R.id.sbar);
        btnBuscar = findViewById(R.id.btn_buscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResults();
            }
        });


    }

    public void openResults(){
        Intent res = new Intent(this, ResutltadosActivity.class);
        String criterio = sBar.getText().toString();
        if(criterio.isEmpty()) criterio = "all";
        res.putExtra("criterio",criterio);
        startActivity(res);
    }

}