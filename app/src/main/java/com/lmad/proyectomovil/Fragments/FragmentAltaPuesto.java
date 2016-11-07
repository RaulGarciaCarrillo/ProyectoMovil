package com.lmad.proyectomovil.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.database.UsuarioDataSource;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentAltaPuesto extends Fragment  implements OnMapReadyCallback{
    Puesto puesto;
    EditText editNameStand, editDescriptionStand;
    CheckBox checkBuffet, checkHamburger, checkHotDog, checkPizza, checkChinese, checkSnack, checkTacos, checkOthers;
    ImageView imgPhotoStand;
    FloatingActionButton btnAddLocal;
    Boolean checkedBuffet, checkedHamburger, checkedHotDog, checkedPizza, checkedChinese, checkedSnack, checkedTacos, checkedOthers;
    ScrollView scrollAlta;

    Geocoder geocoder;
    // Objeto con el cual podremos hacer uso de nuestro mapa de Google
    GoogleMap map;
    // LocationManager: Nos permite obtener la ubicacion del usuario en tiempo real a traves de diferentes metodos.
    LocationManager locationManager;
    MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.alta_puesto, container, false);
        getActivity().setTitle(getResources().getString((R.string.fragmentAdd)));

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Como estamos implementando OnMapReadyCallback (Ver arriba) esto quiere decir que se debe de encontrar
        // el metodo "onMapReady" y una ves que se termine de cargar el mapa se llamara a este metodo para poder
        // comenzar a utilizar el objeto mapa
        mMapView.getMapAsync(this) ;

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
        scrollAlta = (ScrollView) rootView.findViewById(R.id.scrollAlta);
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
                    //Toast.makeText(getContext(), "Buffet", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Hamburger", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Hot-Dog", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Pizza", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Chinese", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Snack", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Tacos", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getContext(), "Others", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_faltan_campos), Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
                    puesto.setIdUsuario(dataSource.getUsuario());

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

                    Toast.makeText(getContext(), getResources().getString(R.string.addStand), Toast.LENGTH_SHORT).show();
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

            } catch(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Problema al regresar de la cámara", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        byte[] bytes = byteArrayOS.toByteArray();
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encode;
    }

    public static Bitmap decodeBase64(String input) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkLocationPermission();
        // Instaciammos nuestor objeto mapa
        map = googleMap;

        // Inicializa nuestro objeto LocationManager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Muestra el boton de "Mi Ubicacion" en el mapa (El tipico circulo azul de google)
        map.setMyLocationEnabled(true);

        // Continuamos obteniendo la ubicacion del usuario para despues mostrar esa ubucacion en el mapa por default
        // pero cuando no se encuentre la ubicacion entonces pondremos una ubicacion fija.
        LatLng currentLocation = null;
        String latlong;
        double latitude, longitude;
        try {

            // Utilizamos el metodo que desarrollamos para obtener la ubicacion del usuario
            currentLocation = getCurrentLocation();
        }catch (SecurityException e) {
            e.printStackTrace();
        }

        // Si se pudo obtener la ubicacion
        if (currentLocation != null) {

            // Movemos la camara para que apunte a otra coordenada diferente e la default
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(currentLocation, 16.f);
            map.moveCamera(cu);
            latitude = currentLocation.latitude;
            longitude = currentLocation.longitude;
            latlong = Double.toString(latitude) + "," + Double.toString(longitude);
            puesto.setCoordenadas(latlong);

            /*String[] latlong =  "-34.8799074,174.7565664".split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]); */

            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName(); ; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                puesto.setDireccion(address);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else { // Si no se pudo obtener la ubicacion

            // Ponemos una ubicacion fija
            LatLng mtyLocation = new LatLng(25.65, -100.29);
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mtyLocation, 16.f);
            map.moveCamera(cu);
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(25.65, -100.29, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName(); ; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                puesto.setDireccion(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Listener para detectar los eventos "Click" dentro del mapa
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            // Este evento nos devuelve la cooordenada geografica donde se dio click dentro del mapa
            @Override
            public void onMapClick(LatLng latLng) {

                // Funcion extra que desarrollamos para agregar marcadores al mapa
                //addMarker("Mi prueba de marcador", latLng,false, true);
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private LatLng getCurrentLocation() throws SecurityException {
        // 1, Obtenemos los proveedores
        List<String> providers = locationManager.getProviders(true);

        Location bestLocation = null;
        for (String provider : providers) {
            //2 ir recorriendo cada proveedor
            // getLastKnowLocation: Obtiene la ultima ubicacion conocidad por el proveedor seleccinado
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                //showToast("Proveedor seleccionado: " + provider);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            //showToast("No se pudo obtener la ubicacion. Espere un momento");
            Toast.makeText(getContext(), "No se pudo obtener la ubicacion. Espere un momento", Toast.LENGTH_SHORT).show();
            return null;
        }
        return new LatLng(bestLocation.getLatitude(), bestLocation.getLongitude());
    }

}
