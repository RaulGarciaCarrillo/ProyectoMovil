package com.lmad.proyectomovil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmad.proyectomovil.R;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentComentarios extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_lista_comentarios, container, false);
        return rootView;
    }
}
