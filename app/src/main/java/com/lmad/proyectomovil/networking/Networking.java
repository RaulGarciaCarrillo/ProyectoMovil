package com.lmad.proyectomovil.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.lmad.proyectomovil.model.Comentario;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.model.Usuario;

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
    static final String SERVER_LISTA_PUESTOS = "http://www.multimediarts.com.mx/foodpoint/listaPuestos.php";
    static final String SERVER_OBTENER_PUESTO = "http://www.multimediarts.com.mx/foodpoint/obtenerPuesto.php";
    static final String SERVER_LISTA_COMENTARIOS = "http://www.multimediarts.com.mx/foodpoint/listaComentarios.php";
    static final String SERVER_AGREGAR_COMENTARIO = "http://www.multimediarts.com.mx/foodpoint/agregarComentario.php";
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

        switch (action){
            case "cargarPuestos":
                Integer idTipoComida = (Integer) params[1];
                List<Puesto> tipoComidaList = cargarPuestos(idTipoComida);
                MyCallback myCallback = (MyCallback) params[2];
                myCallback.onWorkFinish(tipoComidaList);
                break;

            case "cargarDetallePuesto":
                Puesto puesto1 = (Puesto) params[1];
                Puesto puesto2 = cargarDetallePuesto(puesto1.getId());
                MyCallback myCallback1 = (MyCallback) params[2];
                myCallback1.onWorkFinish(puesto2);
                break;

            case "cargarComentariosPuesto":
                Integer idPuesto = (Integer) params[1];
                List<Comentario> comentarioList = cargarComentarios(idPuesto);
                MyCallback myCallback2 = (MyCallback) params[2];
                myCallback2.onWorkFinish(comentarioList);
                break;

            case "agregarComentario":
                Integer idPuestoComentario = (Integer) params[1];
                Integer idUsuarioComentario = (Integer) params[2];
                String descripcionComentario = (String) params[3];
                agregarComentario(idPuestoComentario, idUsuarioComentario, descripcionComentario);
                break;

        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        m_progressDialog.dismiss();
    }

    private List<Puesto> cargarPuestos(Integer idTipoComida) {
        String postParams = "&idTipoComida="+idTipoComida;
        URL url = null;
        HttpURLConnection conn = null;

        List<Puesto> puestoList = new ArrayList<>();

        try {
            url = new URL(SERVER_LISTA_PUESTOS);
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
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String responseString = inputStreamToString(in);
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Puesto puesto = new Puesto();
                    puesto.setId(obj.getInt("idPuesto"));
                    puesto.setCoordenadas(obj.getString("coordenadas"));
                    puesto.setNombre(obj.getString("nombrePuesto"));
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

    private Puesto cargarDetallePuesto(int idPuesto) {
        String postParams = "&idPuesto="+idPuesto;
        URL url = null;
        HttpURLConnection conn = null;

        Puesto puesto = new Puesto();

        try {
            url = new URL(SERVER_OBTENER_PUESTO);
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
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String responseString = inputStreamToString(in);
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                puesto.setNombre(jsonObject.getString("nombre"));
                puesto.setDireccion(jsonObject.getString("direccion"));
                puesto.setFoto(jsonObject.getString("foto"));
                puesto.setDescripcion(jsonObject.getString("descripcion"));
                puesto.setCoordenadas(jsonObject.getString("coordenadas"));
                puesto.setId(jsonObject.getInt("idPuesto"));

            }catch (JSONException e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return puesto;
    }

    private List<Comentario> cargarComentarios(Integer idPuesto) {
        String postParams = "&idPuesto="+idPuesto;
        URL url = null;
        HttpURLConnection conn = null;

        List<Comentario> comentarioList = new ArrayList<>();

        try {
            url = new URL(SERVER_LISTA_COMENTARIOS);
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
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String responseString = inputStreamToString(in);
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Comentario comentario = new Comentario();
                    Puesto puesto = new Puesto();
                    Usuario usuario = new Usuario();

                    usuario.setApodo(obj.getString("apodo"));
                    usuario.setFoto(obj.getString("foto"));
                    comentario.setComentario(obj.getString("descripcion"));
                    comentario.setUsuario(usuario);

                    comentarioList.add(comentario);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return comentarioList;
    }

    private void agregarComentario(Integer idPuesto, Integer idUsuario, String comentario) {
        String postParams = "&idPuesto="+idPuesto+"&idUsuario="+idUsuario+"&descripcion="+comentario;
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_AGREGAR_COMENTARIO);
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
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String responseString = inputStreamToString(in);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Metodo que lee un String desde un InputStream (Convertimos el InputStream del servidor en un String)aadfsa
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
