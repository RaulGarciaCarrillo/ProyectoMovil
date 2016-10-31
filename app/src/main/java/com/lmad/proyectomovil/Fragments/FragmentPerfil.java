package com.lmad.proyectomovil.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.R;

import java.util.Locale;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentPerfil extends Fragment {
    Spinner spinnerLenguaje;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.perfil, container, false);
        getActivity().setTitle("Profile");

        spinnerLenguaje = (Spinner) rootView.findViewById(R.id.spinnerLanguaje);

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


              //Toast.makeText(getContext(), len, Toast.LENGTH_SHORT).show();


              changeFragment(new FragmentPerfil(),len);


          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });

        return rootView;
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        Fragment currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()){
            //Toast.makeText(getContext(), tag, Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión

        ((DrawerLocker)getActivity()).RefreshNav();  //refrescar navigation drawer
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

}
