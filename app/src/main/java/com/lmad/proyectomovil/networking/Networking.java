package com.lmad.proyectomovil.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 10/25/16.
 */

public class Networking extends AsyncTask<Object, Integer, Object> {
    static final String SERVER_LISTA_CATEGORIAS = "http://www.multimediarts.com.mx/foodpoint/listaPuestos.php";
    static final int TIMEOUT = 5000;

    Context m_context;
    ProgressDialog m_progressDialog;

    public Networking(Context m_context) {
        this.m_context = m_context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        m_progressDialog = new ProgressDialog(m_context);
        m_progressDialog.setTitle("Conectando");
        m_progressDialog.setMessage("Espere...");
        m_progressDialog.setCancelable(false); // para que el usuario no pueda quitarlo
        m_progressDialog.show();
    }

    @Override
    protected Object doInBackground(Object... params) {
        String action = (String) params[0];

        if(action.equals("cargarPuestos")){
            List<Puesto> tipoComidaList = cargarPuestos();
            MyCallback myCallback = (MyCallback) params[1];
            myCallback.onWorkFinish(tipoComidaList);
        }else if(action.equals("login")){
        }else{
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        m_progressDialog.dismiss();
    }

    private List<Puesto> cargarPuestos() {
        String postParams = "&userJson=";
        URL url = null;
        HttpURLConnection conn = null;

        List<Puesto> puestoList = new ArrayList<>();

        try {
            url = new URL(SERVER_LISTA_CATEGORIAS);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
               // Toast.makeText(m_context, "Coneccion exitosa", Toast.LENGTH_SHORT).show();
            }
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String responseString = inputStreamToString(in);
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Puesto puesto = new Puesto();
                    puesto.setId(obj.getInt("idPuesto"));
                    puesto.setCoordenadas(obj.getString("coordenadas"));
                    puesto.setNombre(obj.getString("nombre"));
                    puesto.setDescripcion(obj.getString("descripcion"));
                    puesto.setDireccion(obj.getString("direccion"));

                    puesto.setFoto(obj.getString("foto"));


                    puestoList.add(puesto);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return puestoList;
    }

    // Metodo que lee un String desde un InputStream (Convertimos el InputStream del servidor en un String)
    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder response = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while((rLine = rd.readLine()) != null)
            {
                response.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }


}
