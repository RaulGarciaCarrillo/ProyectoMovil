package com.lmad.proyectomovil.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.ShareFeedContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.MainActivity;
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
    TextView tvDescription, tvNameStand;
    ListView lstComennts;
    Button btnPost;
    Button btnCompartir;
    EditText editComment;
    CheckBox checkFavorite;
    Integer checkedFavorite;

    ScrollView scrollPuesto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detalle_puesto, container, false);

        tvNameStand = (TextView) rootView.findViewById(R.id.tvNameStand);
        imgStand = (ImageView) rootView.findViewById(R.id.imgStand);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        lstComennts = (ListView) rootView.findViewById(R.id.lstComennts);
        btnPost = (Button) rootView.findViewById(R.id.btnPost);
        editComment = (EditText) rootView.findViewById(R.id.editComment);
        scrollPuesto = (ScrollView) rootView.findViewById(R.id.scrollPuesto);
        btnCompartir = (Button) rootView.findViewById(R.id.btnCompartir);
        checkFavorite = (CheckBox) rootView.findViewById(R.id.checkFavorite);

        checkedFavorite=0;


        new Networking(rootView.getContext()).execute("cargarDetallePuesto", puesto,new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                final Puesto puesto = (Puesto) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tvNameStand.setText(puesto.getNombre());
                        Bitmap foto = decodeBase64(puesto.getFoto());
                        imgStand.setImageBitmap(foto);
                        tvDescription.setText(puesto.getDescripcion());
                        getActivity().setTitle(puesto.getNombre());

                    }
                });
            }
        });

        ChargeList();

        ChargeFavorite();

        if(checkedFavorite==1) {
            checkFavorite.setChecked(true);
            checkFavorite.setButtonDrawable(R.mipmap.ic_corazon_rojo);
        }
        else {
            checkFavorite.setChecked(false);
            checkFavorite.setButtonDrawable(R.mipmap.ic_corazonvacio);

        }


        checkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedFavorite==0){
                    new Networking(rootView.getContext()).execute("agregarFavorito",1,puesto.getId());
                    checkedFavorite=1;
                    Toast.makeText(getContext(), "Agregado a favoritos.", Toast.LENGTH_SHORT).show();
                    checkFavorite.setButtonDrawable(R.mipmap.ic_corazon_rojo);
                }
               else {
                    new Networking(rootView.getContext()).execute("eliminarFavorito",1,puesto.getId());
                    checkedFavorite=0;
                    Toast.makeText(getContext(), "Eliminado de favoritos.", Toast.LENGTH_SHORT).show();
                    checkFavorite.setButtonDrawable(R.mipmap.ic_corazonvacio);
                }

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Networking(rootView.getContext()).execute("agregarComentario", puesto.getId(), 2, editComment.getText().toString());
                editComment.setText("");
                ChargeList();
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = puesto.getNombre() + " " + puesto.getDescripcion() + " ubicados en " + puesto.getDireccion();

                SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(decodeBase64(puesto.getFoto())).setCaption(msg).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                //ShareDialog.show(getActivity(), content);
                /*ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .setContentTitle(puesto.getNombre())
                        .setContentDescription(puesto.getDescripcion() + "ubicados en " + puesto.getDescripcion())

                        .build();*/
                ShareDialog.show(getActivity(), content);
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

    private void ChargeFavorite (){
        new Networking(rootView.getContext()).execute("obtenerFavorito",1 ,puesto.getId(), new MyCallback() {
            @Override
            public void onWorkFinish(Object data) {
                checkedFavorite = (Integer) data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(checkedFavorite == 1) {
                            checkFavorite.setChecked(true);
                            checkFavorite.setButtonDrawable(R.mipmap.ic_corazon_rojo);
                        }
                        else {
                            checkFavorite.setChecked(false);
                            checkFavorite.setButtonDrawable(R.mipmap.ic_corazonvacio);
                        }
                    }
                });
            }
        });

    }

}

