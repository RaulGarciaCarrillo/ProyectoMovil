package com.lmad.proyectomovil.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.MainActivity;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.database.UsuarioDataSource;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Usuario;
import com.lmad.proyectomovil.networking.Networking;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentPerfil extends Fragment {
    Spinner spinnerLenguaje;
    Usuario usuarioLogeado;

    EditText editProfileUser;
    EditText editProfileEmail;
    EditText editProfilePassword;
    ImageView imgProfilePicture;
    Button btnProfileEdit;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.perfil, container, false);
        getActivity().setTitle(getResources().getString((R.string.fragmentProfile)));

        spinnerLenguaje = (Spinner) rootView.findViewById(R.id.spinnerLanguaje);
        editProfileUser = (EditText) rootView.findViewById(R.id.editProfileUser);
        editProfileEmail = (EditText) rootView.findViewById(R.id.editProfileEmail);
        editProfilePassword = (EditText) rootView.findViewById(R.id.editProfilePassword);
        imgProfilePicture = (ImageView) rootView.findViewById(R.id.imgProfilePicture);
        btnProfileEdit = (Button) rootView.findViewById(R.id.btnProfileEdit);

        usuarioLogeado = new Usuario();
        UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
        usuarioLogeado.setId(dataSource.getUsuario());
        
        final SharedPreferences prefs = getActivity().getSharedPreferences("AppLanguaje", Context.MODE_PRIVATE);
        int languaje = prefs.getInt("languaje", 0);
        spinnerLenguaje.setSelection(languaje);
        spinnerLenguaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String len= spinnerLenguaje.getSelectedItem().toString();
                String SelectLenguaje="";

                if (len.equals("Español")){
                    SelectLenguaje="es";
                } else {
                    SelectLenguaje="en";
                }

                Locale loc = new Locale(SelectLenguaje);
                Locale.setDefault(loc);

                Configuration configuration = new Configuration();
                configuration.locale = loc;

                DisplayMetrics metrics = getActivity().getBaseContext().getResources().getDisplayMetrics();
                getActivity().getBaseContext().getResources().updateConfiguration(configuration,metrics);
                
                prefs.edit().putInt("lenguaje", position).commit();
                
                //changeFragment(new FragmentPerfil(),len);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuarioModicicar = new Usuario();
                usuarioModicicar.setCorreo(editProfileEmail.getText().toString());
                usuarioModicicar.setId(usuarioLogeado.getId());
                usuarioModicicar.setContrasenia(editProfilePassword.getText().toString());
                usuarioModicicar.setApodo(editProfileUser.getText().toString());
                Bitmap foto = ((BitmapDrawable) imgProfilePicture.getDrawable()).getBitmap();
                String fotoBase64 = encodeToBase64(foto);
                usuarioModicicar.setFoto(fotoBase64);
                new Networking(v.getContext()).execute("modificarUsuario", usuarioModicicar);
                Toast.makeText(getContext(), getResources().getString(R.string.modifyProfile), Toast.LENGTH_SHORT).show();

            }
        });

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });

        obtenerUsuario();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //cuando ya regresemos de la camara
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgProfilePicture.setImageBitmap(bitmap);
            } catch(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Problema al regresar de la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        Fragment currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()){
            return;
        }

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión

        ((DrawerLocker)getActivity()).RefreshNav();  //refrescar navigation drawer
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void obtenerUsuario(){
        new Networking(rootView.getContext()).execute("obtenerUsuario", usuarioLogeado,new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final Usuario usuarioA = (Usuario) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap foto = decodeBase64(usuarioA.getFoto());
                        imgProfilePicture.setImageBitmap(foto);
                        editProfileUser.setText(usuarioA.getApodo());
                        editProfileEmail.setText(usuarioA.getCorreo());
                        editProfilePassword.setText(usuarioA.getContrasenia());
                    }
                });
            }
        });
    }

}
