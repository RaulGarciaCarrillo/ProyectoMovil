package com.lmad.proyectomovil.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.MainActivity;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.ComentarioAdapter;
import com.lmad.proyectomovil.model.Comentario;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Usuario;
import com.lmad.proyectomovil.networking.Networking;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentRegistro extends Fragment {
    Button btnCancel, btnSignIn;
    ImageView imgRegisterPicture;
    EditText editUserName;
    EditText editUser;
    EditText editPassword;
    EditText editConfirmPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.registro, container, false);

        getActivity().setTitle(getResources().getString(R.string.fragmentRegister));

        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSignIn = (Button) rootView.findViewById(R.id.btnSignIn);

        imgRegisterPicture = (ImageView) rootView.findViewById(R.id.imgRegisterPicture);
        editUserName = (EditText) rootView.findViewById(R.id.editUserName);
        editUser = (EditText) rootView.findViewById(R.id.editUser);
        editPassword = (EditText) rootView.findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) rootView.findViewById(R.id.editConfirmPassword);

        imgRegisterPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
            }
        });



        //botón Cancelar
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new FragmentLogin(), "login");
            }
        });

        //botón Registrarse
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                    if(editPassword.getText().toString().equals("") ||
                        editConfirmPassword.getText().toString().equals("") ||
                        editUser.getText().toString().equals("") ||
                        editUserName.getText().toString().equals("")) {
                            Toast.makeText(getContext(), getResources().getString(R.string.toast_faltan_campos), Toast.LENGTH_SHORT).show();
                            return;
                    } else {
                        Usuario usuario = new Usuario();
                        Bitmap foto = ((BitmapDrawable) imgRegisterPicture.getDrawable()).getBitmap();
                        String fotoBase64 = encodeToBase64(foto);
                        usuario.setFoto(fotoBase64);
                        usuario.setApodo(editUserName.getText().toString());
                        usuario.setContrasenia(editPassword.getText().toString());
                        usuario.setCorreo(editUser.getText().toString());

                        new Networking(v.getContext()).execute("agregarUsuario", usuario, new MyCallback() {
                            @Override
                            public void onWorkFinish(Object data) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        });
                        changeFragment(new FragmentMenuPrincipal(), "inicio");
                    }
                } else{
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_contrasenia), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //cuando ya regresemos de la camara
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgRegisterPicture.setImageBitmap(bitmap);
            } catch(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Problema al regresar de la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
