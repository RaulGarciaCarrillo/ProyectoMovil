package com.lmad.proyectomovil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.PuestoAdapter;
import com.lmad.proyectomovil.database.UsuarioDataSource;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentListaFavoritos extends Fragment {
    Integer idUsuario;
    ListView lstFavoritos;
    TextView noResultadosFav;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.lista_favoritos, container, false);
        getActivity().setTitle(getResources().getString(R.string.fragmentFavorites));

        lstFavoritos = (ListView) rootView.findViewById(R.id.lstFavoritos);
        noResultadosFav = (TextView) rootView.findViewById(R.id.noResultadosFav);
        UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
        idUsuario = dataSource.getUsuario();
        new Networking(rootView.getContext()).execute("cargarFavoritos", idUsuario, new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final List<Puesto> puestoList = (List<Puesto>) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (puestoList.size() != 0) {
                            final PuestoAdapter puestoAdapter = new PuestoAdapter(puestoList);
                            lstFavoritos.setAdapter(puestoAdapter);
                        }else {
                            noResultadosFav.setText(R.string.tv_noResultados);
                        }
                    }
                });
            }
        });

        lstFavoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Puesto puesto = (Puesto) lstFavoritos.getAdapter().getItem(position);
                FragmentDetallePuesto fragmentDetallePuesto = new FragmentDetallePuesto();
                fragmentDetallePuesto.setPuesto(puesto);
                changeFragment(fragmentDetallePuesto, "detallePuesto");
            }
        });

        return rootView;
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)
        ft.commit();//cerrar conexión
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    changeFragment(new FragmentMenuPrincipal(), "inicio");
                    return true;
                }
                return false;
            }
        });
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}