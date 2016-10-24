package com.lmad.proyectomovil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lmad.proyectomovil.R;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentAltaPuesto extends Fragment {
    FloatingActionButton btnAddLocal;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alta_puesto, container, false);

        btnAddLocal = (FloatingActionButton) rootView.findViewById(R.id.btnAddLocal);

        //botón Agregar puesto
        btnAddLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Puesto agregado exitosamente.",Toast.LENGTH_SHORT).show();
                changeFragment(new FragmentMenuPrincipal(), "inicio");
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
}
