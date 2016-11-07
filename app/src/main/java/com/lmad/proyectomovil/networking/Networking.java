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
    static final String SERVER_AGREGAR_USUARIO = "http://www.multimediarts.com.mx/foodpoint/agregarUsuario.php";
    static final String SERVER_MODIFICAR_USUARIO = "http://www.multimediarts.com.mx/foodpoint/modificarUsuario.php";
    static final String SERVER_VALIDAR_LOGIN = "http://www.multimediarts.com.mx/foodpoint/validarLogin.php";
    static final String SERVER_AGREGAR_PUESTO = "http://www.multimediarts.com.mx/foodpoint/agregarPuesto.php";
    static final String SERVER_AGREGAR_PUESTO_COMIDA = "http://www.multimediarts.com.mx/foodpoint/agregarPuesto_Comida.php";
    static final String SERVER_AGREGAR_FAVORITO = "http://www.multimediarts.com.mx/foodpoint/agregarFavorito.php";
    static final String SERVER_ELIMINAR_FAVORITO = "http://www.multimediarts.com.mx/foodpoint/eliminarFavorito.php";
    static final String SERVER_OBTENER_FAVORITO = "http://www.multimediarts.com.mx/foodpoint/obtenerFavorito.php";
    static final String SERVER_LISTA_FAVORITOS = "http://www.multimediarts.com.mx/foodpoint/listaFavoritos.php";

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

            case "agregarUsuario":
                Usuario usuarioAgregar = (Usuario) params[1];
                agregarUsuario(usuarioAgregar);
                break;

            case "modificarUsuario":
                Usuario usuarioModificar = (Usuario) params[1];
                modificarUsuario(usuarioModificar);
                break;

            case "validacionUsuario":
                String correo = (String) params[1];
                String contra = (String) params[2];
                Integer validacion = validacionUsuario(correo,contra);
                MyCallback myCallback4 = (MyCallback) params[3];
                myCallback4.onWorkFinish(validacion);
                break;


            case "agregarPuesto":
                Puesto puestoAgregar = (Puesto) params[1];
                agregarPuesto(puestoAgregar);
                break;

            case "agregarPuestoComida":
                Integer idPuestoTipoComida = (Integer) params[1];
                agregarPuestoComida(idPuestoTipoComida);
                break;

            case "agregarFavorito":
                Integer idUsuarioAF = (Integer) params[1];
                Integer idPuestoAF = (Integer) params [2];
                agregarFavorito(idUsuarioAF,idPuestoAF);
                break;

            case "eliminarFavorito":
                Integer idUsuarioEF = (Integer) params[1];
                Integer idPuestoEF = (Integer) params [2];
                eliminarFavorito(idUsuarioEF,idPuestoEF);
                break;

            case "obtenerFavorito":
                Integer idUsuarioOF = (Integer) params[1];
                Integer idPuestoOF = (Integer) params [2];
                Integer esFavorito = obtenerFavorito(idUsuarioOF,idPuestoOF);
                MyCallback myCallback3 = (MyCallback) params[3];
                myCallback3.onWorkFinish(esFavorito);
                break;

            case "cargarFavoritos":
                Integer idFavorito = (Integer) params[1];
                List<Puesto> favoritosList = cargarFavoritos(idFavorito);
                MyCallback myCallbackF = (MyCallback) params[2];
                myCallbackF.onWorkFinish(favoritosList);
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

    private void agregarUsuario(Usuario usuario) {
        String postParams = "&apodo="+usuario.getApodo()+"&correo="+usuario.getCorreo()+"&contrasena="+usuario.getContrasenia()+"&foto="+usuario.getFoto();
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_AGREGAR_USUARIO);
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

    private void modificarUsuario(Usuario usuario) {
        String postParams = "&idUsuario="+usuario.getId()+"&apodo="+usuario.getApodo()+"&correo="+usuario.getCorreo()+"&contrasena="+usuario.getContrasenia()+"&foto="+usuario.getFoto();
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_MODIFICAR_USUARIO);
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

    private Integer validacionUsuario (String usuario, String contra ){
        String postParams = "&correo=" +usuario+ "&contrasena=" +contra;
        URL url = null;
        HttpURLConnection conn = null;
        Integer validacion = 0;

        try{
            url = new URL(SERVER_VALIDAR_LOGIN);
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
                validacion=(jsonObject.getInt("idUsuario"));

            }catch (JSONException e){
                e.printStackTrace();
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return validacion;
    }

    private void agregarPuesto(Puesto puesto){
        String postParams ="&idUsuario="+1+"&nombre="+puesto.getNombre()+"&descripcion="+puesto.getDescripcion()
                +"&direccion="+puesto.getDireccion()+ "&coordenadas="+puesto.getCoordenadas()+"&foto="+puesto.getFoto();
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_AGREGAR_PUESTO);
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

    private void agregarPuestoComida(Integer idPuestoTipoComida){
        String postParams ="&idTipoComida="+idPuestoTipoComida;
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_AGREGAR_PUESTO_COMIDA);
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

    private void agregarFavorito (Integer idUsuario, Integer idPuesto){
        String postParams ="&idUsuario="+idUsuario+"&idPuesto="+idPuesto;
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_AGREGAR_FAVORITO);
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

    private void eliminarFavorito (Integer idUsuario, Integer idPuesto){
        String postParams ="&idUsuario="+idUsuario+"&idPuesto="+idPuesto;
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(SERVER_ELIMINAR_FAVORITO);
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

    private Integer obtenerFavorito (Integer idUsuario, Integer idPuesto){
        String postParams ="&idUsuario="+idUsuario+"&idPuesto="+idPuesto;
        URL url = null;
        HttpURLConnection conn = null;
        Integer fav=0;

        try {
            url = new URL(SERVER_OBTENER_FAVORITO);
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
                fav=(jsonObject.getInt("Favorito"));

            }catch (JSONException e){
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return fav;
    }

    private List<Puesto> cargarFavoritos(Integer idUsuario) {
        String postParams = "&idUsuario="+idUsuario;
        URL url = null;
        HttpURLConnection conn = null;

        List<Puesto> puestoList = new ArrayList<>();

        try {
            url = new URL(SERVER_LISTA_FAVORITOS);
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
                    puesto.setNombre(obj.getString("NombrePuesto"));
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
