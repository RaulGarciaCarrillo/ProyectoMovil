package com.lmad.proyectomovil.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentAltaPuesto extends Fragment {
    Puesto puesto;
    EditText editNameStand, editDescriptionStand;
    CheckBox checkBuffet, checkHamburger, checkHotDog, checkPizza, checkChinese, checkSnack, checkTacos, checkOthers;
    ImageView imgPhotoStand;
    FloatingActionButton btnAddLocal;

    Boolean checkedBuffet, checkedHamburger, checkedHotDog, checkedPizza, checkedChinese, checkedSnack, checkedTacos, checkedOthers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.alta_puesto, container, false);
        getActivity().setTitle(getResources().getString((R.string.fragmentAdd)));

        puesto = new Puesto();
        editNameStand = (EditText) rootView.findViewById(R.id.editNameStand);
        editDescriptionStand = (EditText) rootView.findViewById(R.id.editDescriptionStand);
        checkBuffet = (CheckBox) rootView.findViewById(R.id.checkBuffet);
        checkHamburger = (CheckBox) rootView.findViewById(R.id.checkHamburgers);
        checkHotDog = (CheckBox) rootView.findViewById(R.id.checkHotDog);
        checkPizza = (CheckBox) rootView.findViewById(R.id.checkPizza);
        checkChinese = (CheckBox) rootView.findViewById(R.id.checkChinese);
        checkSnack = (CheckBox) rootView.findViewById(R.id.checkSnack);
        checkTacos = (CheckBox) rootView.findViewById(R.id.checkTacos);
        checkOthers = (CheckBox) rootView.findViewById(R.id.checkOthers);
        imgPhotoStand = (ImageView) rootView.findViewById(R.id.imgPhotoStand);
        btnAddLocal = (FloatingActionButton) rootView.findViewById(R.id.btnAddLocal);
        checkedBuffet=false;
        checkedHamburger=false;
        checkedHotDog = false;
        checkedPizza= false;
        checkedChinese=false;
        checkedSnack=false;
        checkedTacos=false;
        checkedOthers=false;

        checkBuffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedBuffet==false){
                    Toast.makeText(getContext(), "Buffet", Toast.LENGTH_SHORT).show();
                    checkedBuffet=true;
                }
                else
                   checkedBuffet = false;
            }
        });

        checkHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedHamburger==false){
                    Toast.makeText(getContext(), "Hamburger", Toast.LENGTH_SHORT).show();
                    checkedHamburger=true;
                }
                else
                    checkedHamburger = false;
            }
        });

        checkHotDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedHotDog==false){
                    Toast.makeText(getContext(), "Hot-Dog", Toast.LENGTH_SHORT).show();
                    checkedHotDog=true;
                }
                else
                    checkedHotDog = false;
            }
        });

        checkPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedPizza==false){
                    Toast.makeText(getContext(), "Pizza", Toast.LENGTH_SHORT).show();
                    checkedPizza=true;
                }
                else
                    checkedPizza = false;
            }
        });

        checkChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedChinese==false){
                    Toast.makeText(getContext(), "Chinese", Toast.LENGTH_SHORT).show();
                    checkedChinese=true;
                }
                else
                    checkedChinese = false;
            }
        });

        checkSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedSnack==false){
                    Toast.makeText(getContext(), "Snack", Toast.LENGTH_SHORT).show();
                    checkedSnack=true;
                }
                else
                    checkedSnack = false;
            }
        });

        checkTacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedTacos==false){
                    Toast.makeText(getContext(), "Tacos", Toast.LENGTH_SHORT).show();
                    checkedTacos=true;
                }
                else
                    checkedTacos = false;
            }
        });

        checkOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedOthers==false){
                    Toast.makeText(getContext(), "Others", Toast.LENGTH_SHORT).show();
                    checkedOthers=true;
                }
                else
                    checkedOthers = false;
            }
        });


        imgPhotoStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Validamos que si exista ese intent antes de intentar abrirlo ya que luego puede marcar error
                // si no existe el intent y no lo validamos antes.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Como vamos a obtener un "resultado" (la imagen de la camara) abrimos el activity
                    // con el metodo "startActivityForResult() recuerden que el 2do parametro es el "id" que se le da"
                    //(intent, requestCode identificador, )
                    startActivityForResult(intent, 1);
                }
            }
        });

        //botón Agregar puesto
        btnAddLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puesto.setNombre(editNameStand.getText().toString());
                puesto.setDescripcion(editDescriptionStand.getText().toString());
                Bitmap foto = ((BitmapDrawable)imgPhotoStand.getDrawable()).getBitmap();
                String fotoBase64 = encodeToBase64(foto);
                puesto.setFoto(fotoBase64);

                if ((checkedBuffet==false && checkedHamburger==false && checkedHotDog==false &&  checkedPizza==false
                        && checkedChinese==false &&  checkedSnack==false && checkedTacos==false &&  checkedOthers==false)
                        || puesto.getNombre().isEmpty() || puesto.getDescripcion().isEmpty() || puesto.getFoto().isEmpty()){
                    Toast.makeText(getContext(), "¡Faltan campos por llenar!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    new Networking(rootView.getContext()).execute("agregarPuesto", puesto);

                    if(checkedBuffet==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 1);
                    if(checkedChinese==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 2);
                    if(checkedHamburger==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 3);
                    if(checkedHotDog==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 4);
                    if(checkedPizza==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 5);
                    if(checkedSnack==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 6);
                    if(checkedTacos==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 7);
                    if(checkedOthers==true)
                        new Networking(rootView.getContext()).execute("agregarPuestoComida", 8);

                    Toast.makeText(getContext(), "Puesto agregado exitosamente.", Toast.LENGTH_SHORT).show();
                    changeFragment(new FragmentMenuPrincipal(), "inicio");
                }
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //cuando ya regresemos de la camara
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgPhotoStand.setImageBitmap(bitmap);
                Toast.makeText(getContext(),"Foto guardada", Toast.LENGTH_SHORT).show();

            } catch(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Problema al regresar de la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        byte[] bytes = byteArrayOS.toByteArray();
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encode;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }
}
