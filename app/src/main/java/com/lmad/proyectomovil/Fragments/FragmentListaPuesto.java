package com.lmad.proyectomovil.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.PuestoAdapter;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentListaPuesto extends Fragment {

    ListView lstPuestos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.lista_puesto, container, false);
        lstPuestos = (ListView) rootView.findViewById(R.id.lstPuestos);

        new Networking(rootView.getContext()).execute("cargarPuestos", new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final List<Puesto> puestoList = (List<Puesto>) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (puestoList.size() != 0) {
                            final PuestoAdapter puestoAdapter = new PuestoAdapter(puestoList);
                            lstPuestos.setAdapter(puestoAdapter);
                        }
                    }
                });
            }
        });

        lstPuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lstPuestos.getAdapter().getItem(position);
                changeFragment(new FragmentDetallePuesto(), "detalle");
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
