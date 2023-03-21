package com.eddybelduma.examenfinaleddybelduma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelos.Usuarios;

public class ActualizarUsu extends AppCompatActivity {

    public static String idAPI;
    String nombreAPI, emailAPI, generoAPI, statusAPI;
    boolean estadoAC = true;
    ArrayList<Usuarios> datos =new ArrayList<>();

    String Token = "60f7888870d4748003bf0edf7df6e43629936a98c9078252f6af54a23030d694";

    TextView idVer;
    TextView nombre;
    TextView email;
    TextView idbuscar;
    Spinner genero;
    RadioButton activo;
    RadioButton inactivo;
    RadioGroup grupo;
    Button buscar;
    Button actuali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_usu);

        idbuscar = findViewById(R.id.txtBuscarId);
        idVer = findViewById(R.id.txtID);
        nombre = findViewById(R.id.txtNombre);
        email = findViewById(R.id.txtemail);
        genero = findViewById(R.id.spgenero);
        activo = findViewById(R.id.rdBtActivo);
        inactivo = findViewById(R.id.rdBtInactivo);
        grupo = findViewById(R.id.GroupRD);
        buscar = findViewById(R.id.btBuscar);
        actuali = findViewById(R.id.btActualizar);

        itemsSpinner();

        RequestQueue queue = Volley.newRequestQueue(this);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatos(idbuscar.getText().toString());
            }
        });

        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == activo.getId()) {
                    estadoAC=true;
                } else if (checkedId == inactivo.getId()) {
                    estadoAC=false;
                }
            }
        });

        actuali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idAPI = idbuscar.getText().toString();
                actualizarDatos(idbuscar, nombre, email, genero, estadoAC);
            }
        });
    }

    private void itemsSpinner() {
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Ninguno");
        spinnerItems.add("female");
        spinnerItems.add("male");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner SpinnGenero = findViewById(R.id.spgenero);
        SpinnGenero.setAdapter(spinnerAdapter);
    }

    private void actualizarDatos(TextView identext, TextView nombre, TextView email, Spinner generoAC, Boolean esta) {

        String act1 = identext.getText().toString();
        String act2 = nombre.getText().toString();
        String act3 = email.getText().toString();
        String act4 = generoAC.getSelectedItem().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", act1);
            jsonObject.put("name", act2);
            jsonObject.put("email",act3 );
            jsonObject.put("gender", act4);
            if(esta){
                jsonObject.put("status", "active");
            }else{
                jsonObject.put("status", "inactive");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonObject.toString();
        String url = "https://gorest.co.in/public/v2/users/"+act1;
        System.out.println(url);

        StringRequest request = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta de la API
                        Log.d("RESPONSE", "La respuesta es: " + response + url);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de la API
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody == null ? null : requestBody.getBytes(Charset.forName("utf-8"));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+ Token);
                return headers;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void manejaJson(@NonNull JSONArray jsonArray){
        Usuarios usuarios=new Usuarios();
        for (int i=0; i<jsonArray.length();i++){
            JSONObject jsonObject=null;
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
        //arrayAdapter.notifyDataSetChanged();
    }

    private void obtenerDatos(String id){
        if(id.isEmpty()){
            Toast.makeText(ActualizarUsu.this, "ID incorrecto o no encontrado", Toast.LENGTH_SHORT).show();
        }else{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://gorest.co.in/public/v2/users/"+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Obtener el atributo deseado de la respuesta
                    String atributo = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        idVer.setText(jsonObject.getString("id"));
                        nombre.setText(jsonObject.getString("name"));
                        email.setText(jsonObject.getString("email"));
                        generoAPI = jsonObject.getString("gender").toString();
                        if(generoAPI.equals("female")){
                            genero.setSelection(1);
                        }else{
                            genero.setSelection(2);
                        }
                        statusAPI = jsonObject.getString("status").trim();
                        if (statusAPI.equals("active")) {
                            activo.setChecked(true);
                        } else if (statusAPI.equals("inactive")) {
                            inactivo.setChecked(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Configurar el atributo en el TextView

                }
            }, null);
            idAPI = idVer.getText().toString();
            requestQueue.add(stringRequest);
        }
    }

}