package com.lmad.proyectomovil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lmad.proyectomovil.MainActivity;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.PuestoAdapter;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentListaPuesto extends Fragment {

    ListView lstPuestos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        return rootView;
    }

    private void initListViewContacts() {

    }
}
