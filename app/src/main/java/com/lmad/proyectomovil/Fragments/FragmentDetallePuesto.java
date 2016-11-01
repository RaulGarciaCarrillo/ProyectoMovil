package com.lmad.proyectomovil.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.ComentarioAdapter;
import com.lmad.proyectomovil.adapter.PuestoAdapter;
import com.lmad.proyectomovil.model.Comentario;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentDetallePuesto extends Fragment{

    Puesto puesto;

    View rootView;
    ImageView imgStand;
    TextView tvDescription;
    ListView lstComennts;
    Button btnPost;
    EditText editComment;

    ScrollView scrollPuesto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detalle_puesto, container, false);

        imgStand = (ImageView) rootView.findViewById(R.id.imgStand);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        lstComennts = (ListView) rootView.findViewById(R.id.lstComennts);
        btnPost = (Button) rootView.findViewById(R.id.btnPost);
        editComment = (EditText) rootView.findViewById(R.id.editComment);
        scrollPuesto = (ScrollView) rootView.findViewById(R.id.scrollPuesto);

        new Networking(rootView.getContext()).execute("cargarDetallePuesto", puesto,new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final Puesto puesto = (Puesto) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap foto = decodeBase64(puesto.getFoto());
                        imgStand.setImageBitmap(foto);
                        tvDescription.setText(puesto.getDescripcion());
                        getActivity().setTitle(puesto.getNombre());

                    }
                });
            }
        });

        ChargeList();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Networking(rootView.getContext()).execute("agregarComentario", puesto.getId(), 18, editComment.getText().toString());
                editComment.setText("");
                ChargeList();
            }
        });

        return rootView;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    private void ChargeList (){
        new Networking(rootView.getContext()).execute("cargarComentariosPuesto", puesto.getId(), new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final List<Comentario> comentarioList = (List<Comentario>) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (comentarioList.size() != 0) {
                            final ComentarioAdapter comentarioAdapter = new ComentarioAdapter(comentarioList);
                            lstComennts.setAdapter(comentarioAdapter);
                            setListViewHeightBasedOnChildren(lstComennts);
                        }
                    }
                });
            }
        });

    }

}

