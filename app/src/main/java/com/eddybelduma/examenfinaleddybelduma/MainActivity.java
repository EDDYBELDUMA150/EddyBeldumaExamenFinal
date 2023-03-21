package com.eddybelduma.examenfinaleddybelduma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modelos.Usuarios;

public class MainActivity extends AppCompatActivity {

    ArrayList<Usuarios> datos =new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView listausuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listausuarios = findViewById(R.id.listusu);
        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, datos);
        listausuarios.setAdapter(arrayAdapter);
        obtenerDatos();
    }

    private void manejaJson(@NonNull JSONArray jsonArray){
        for (int i=0; i<jsonArray.length();i++){
            JSONObject jsonObject=null;
            Usuarios usuarios=new Usuarios();
            try {
                jsonObject=jsonArray.getJSONObject(i);
                usuarios.setId(jsonObject.getInt("id"));
                usuarios.setName(jsonObject.getString("name"));
                usuarios.setEmail(jsonObject.getString("email"));
                usuarios.setGenero(jsonObject.getString("gender"));
                usuarios.setEstado(jsonObject.getString("status"));
                datos.add(usuarios);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void obtenerDatos(){
        String url="https://gorest.co.in/public/v2/users";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //obtengo el json respuesta
                //MANEJAMOS EL JSON
                manejaJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //obtengo un error si es que se da
                System.out.println(error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}