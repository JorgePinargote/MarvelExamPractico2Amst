package com.example.heroesamst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResutltadosActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList<String> heroesNames;
    private ArrayList<String> heroesId;
    private RequestQueue ListaRequest = null;
    private TextView contador;
    Map<String, JSONObject> map;

    //map.put("dog", "type of animal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resutltados);
        listview = (ListView) findViewById(R.id.ListRes);
        contador = (TextView) findViewById(R.id.txt_contador);
        this.heroesNames = new ArrayList<>();
        this.heroesId = new ArrayList<>();
        ListaRequest = Volley.newRequestQueue(this);
        map = new HashMap<String, JSONObject>();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    Intent perfil = new Intent(this, PerfilActivity.class);
                    perfil.putExtra("id", heroesId.get(position));
                    startActivity(perfil);
                }catch (Exception e){
                    Log.e("ResultadosActivity", e.getMessage());
                    Toast.makeText(ResutltadosActivity.this, "Has pulsado: "+ position, Toast.LENGTH_LONG).show();
                }
            }
        });

        buscarHeroes();


    }

    private void buscarHeroes(){
        String url_registros = "https://www.superheroapi.com/api.php/3519810141403606/search/"  + getIntent().getStringExtra("criterio");
        JsonObjectRequest requestHeroes =
                new JsonObjectRequest( Request.Method.GET,
                        url_registros, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ActualizarLista(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error);
                            }
                        });
        ListaRequest.add(requestHeroes);
    }

    private void ActualizarLista(JSONObject heroes){
        int cont = 0;
        try {
            JSONArray array = (JSONArray)heroes.get("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject heroe =(JSONObject) array.get(i);
                map.put(heroe.get("id").toString(), heroe);
                this.heroesId.add(heroe.get("id").toString());
                this.heroesNames.add(heroe.get("name").toString());
                cont++;
            }
            this.contador.setText(String.valueOf(cont));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  this.heroesNames);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*try {

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}
