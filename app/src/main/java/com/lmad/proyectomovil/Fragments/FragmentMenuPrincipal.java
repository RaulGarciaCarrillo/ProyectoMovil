package com.lmad.proyectomovil.Fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Context;
import android.widget.Toast;


import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentMenuPrincipal extends Fragment {
    ImageView tvTacos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_principal, container, false);
        setHasOptionsMenu(true);

        ((DrawerLocker)getActivity()).setDrawerEnable(true);

        tvTacos = (ImageView) rootView.findViewById(R.id.imgTipoC1);

        //dar clic en Tacos
        tvTacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new FragmentListaPuesto(),"detalle");
            }
        });




        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inicio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAdd:
                changeFragment(new FragmentAltaPuesto(), "alta");
                break;
        }

        return true;
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }


    // Metodo util para saber si hay conectividad o no.
    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }


    void showToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


}
