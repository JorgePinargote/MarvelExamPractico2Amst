package com.example.heroesamst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    public BarChart graficoBarras;
    private RequestQueue ListaRequest = null;
    private String url = "https://www.superheroapi.com/api.php/3519810141403606/";
    private TextView txt_nombre;
    private TextView txt_nombreCompleto;
    private PerfilActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //inicializar componentes
        context = this;
        ListaRequest = Volley.newRequestQueue(this);

        this.iniciarGrafico();
        this.solicitarInformacion();

    }

    public void iniciarGrafico() {
        graficoBarras = findViewById(R.id.barChart);
        graficoBarras.getDescription().setEnabled(false);
        graficoBarras.setMaxVisibleValueCount(60);
        graficoBarras.setPinchZoom(false);
        graficoBarras.setDrawBarShadow(false);
        graficoBarras.setDrawGridBackground(false);
        XAxis xAxis = graficoBarras.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        graficoBarras.getAxisLeft().setDrawGridLines(false);
        graficoBarras.animateY(1500);
        graficoBarras.getLegend().setEnabled(false);

        txt_nombreCompleto = (TextView)findViewById(R.id.txt_nombreCompleto);
        txt_nombreCompleto.setText("Gafico");
    }
    public void solicitarInformacion(){;
        String urlRequest = url + getIntent().getStringExtra("id");
        txt_nombre = (TextView)findViewById(R.id.txt_nombre);
        txt_nombre.setText(urlRequest);
        JsonObjectRequest requestInformacion =
                new JsonObjectRequest( Request.Method.GET,
                        urlRequest, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    actualizar(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
                );
        ListaRequest.add(requestInformacion);
    }

    private void actualizar(JSONObject object) throws JSONException {
        String nombre = object.getString("name");
        JSONObject biografia = object.getJSONObject("biography");
        String nCompleto = biografia.getString("full-name");
        JSONObject power = object.getJSONObject("powerstats");


        txt_nombre = (TextView)findViewById(R.id.txt_nombre);
        txt_nombre.setText(nombre);

        txt_nombreCompleto = (TextView)findViewById(R.id.txt_nombreCompleto);
        txt_nombreCompleto.setText(nCompleto);
        actualizarGrafico(power);

    }

    public void actualizarGrafico(JSONObject power){
        ArrayList<BarEntry> datos = new ArrayList<>();

        try {
            datos.add(new BarEntry(1,Integer.parseInt(power.getString("intelligence"))));
            datos.add(new BarEntry(2,Integer.parseInt(power.getString("strength"))));
            datos.add(new BarEntry(3,Integer.parseInt(power.getString("speed"))));
            datos.add(new BarEntry(4,Integer.parseInt(power.getString("durability"))));
            datos.add(new BarEntry(5,Integer.parseInt(power.getString("power"))));
            datos.add(new BarEntry(6,Integer.parseInt(power.getString("combat"))));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BarDataSet datosDataSet;
        if ( graficoBarras.getData() != null &&
                graficoBarras.getData().getDataSetCount() > 0) {
            datosDataSet = (BarDataSet)
                    graficoBarras.getData().getDataSetByIndex(0);
            datosDataSet.setValues(datos);
            graficoBarras.getData().notifyDataChanged();
            graficoBarras.notifyDataSetChanged();
        } else {
            datosDataSet = new BarDataSet(datos, "Data Set");
            datosDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            datosDataSet.setDrawValues(true);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(datosDataSet);
            BarData data = new BarData(dataSets);
            graficoBarras.setData(data); graficoBarras.setFitBars(true);
        }
        graficoBarras.invalidate();


    }

}