package com.lmad.proyectomovil.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.MainActivity;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.database.UsuarioDataSource;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.networking.Networking;

import static android.R.attr.data;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentLogin extends Fragment {

    View rootView;
    Button btnLogIn,btnRegister;
    EditText editUser, editPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.login, container, false);

        ((DrawerLocker)getActivity()).setDrawerEnable(false);  //bloquear navigation drawer

        getActivity().setTitle(getResources().getString((R.string.fragmentLogin)));

        btnLogIn = (Button) rootView.findViewById(R.id.btnLogin);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        editUser = (EditText) rootView.findViewById(R.id.editUser);
        editPassword = (EditText) rootView.findViewById(R.id.editPassword);


        //botón Inicia Sesión
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if (editUser.getText().toString().equals("") || editPassword.getText().toString().equals("")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.toast_faltan_campos), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        new Networking(rootView.getContext()).execute("validacionUsuario", editUser.getText().toString(), editPassword.getText().toString(), new MyCallback() {
                            @Override
                            public void onWorkFinish(Object data) {
                                final Integer idUsuario = (Integer) data;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (idUsuario == 0) {
                                            Toast.makeText(getContext(), getResources().getString(R.string.toastLogin), Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            changeFragment(new FragmentMenuPrincipal(), "inicio");
                                            UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
                                            dataSource.insertUsuario(idUsuario);
                                            Integer i = dataSource.getUsuario();
                                            //Toast.makeText(getContext(), i.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_noInternet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //botón Regístrate
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    changeFragment(new FragmentRegistro(), "registro");
                }else{
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_noInternet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    // Metodo util para saber si hay conectividad o no.
    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }
    /* private void loginopi (){
        new Networking(rootView.getContext()).execute("validacionUsuario", editUser.getText().toString(),editPassword.getText().toString(), new MyCallback() {
            @Override
            public void onWorkFinish(final Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Integer idusuario = (Integer) data;
                        if(idusuario == 0){
                            changeFragment(new FragmentMenuPrincipal(), "inicio");
                        }else{
                            Toast.makeText(getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                        changeFragment(new FragmentMenuPrincipal(), "inicio");

                    }
                });
            }
        });

    }*/
}

