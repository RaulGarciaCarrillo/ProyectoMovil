package com.lmad.proyectomovil.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.KeyEvent;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lmad.proyectomovil.DrawerLocker;
import com.lmad.proyectomovil.MainActivity;
import com.lmad.proyectomovil.R;
import com.lmad.proyectomovil.adapter.ComentarioAdapter;
import com.lmad.proyectomovil.adapter.PuestoAdapter;
import com.lmad.proyectomovil.database.UsuarioDataSource;
import com.lmad.proyectomovil.model.Comentario;
import com.lmad.proyectomovil.model.MyCallback;
import com.lmad.proyectomovil.model.Puesto;
import com.lmad.proyectomovil.networking.Networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mario on 22/10/2016.
 */

public class FragmentDetallePuesto extends Fragment implements OnMapReadyCallback {

    Puesto puesto;

    View rootView;
    ImageView imgStand;
    TextView tvDescription, tvNameStand, tvAddress;
    ListView lstComennts;
    Button btnPost;
    Button btnCompartir;
    EditText editComment;
    CheckBox checkFavorite;
    Integer checkedFavorite;

    ScrollView scrollPuesto;

    Geocoder geocoder;
    // Objeto con el cual podremos hacer uso de nuestro mapa de Google
    GoogleMap map;
    // LocationManager: Nos permite obtener la ubicacion del usuario en tiempo real a traves de diferentes metodos.
    LocationManager locationManager;
    MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detalle_puesto, container, false);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        mMapView = (MapView) rootView.findViewById(R.id.mapViewD);
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

        tvNameStand = (TextView) rootView.findViewById(R.id.tvNameStand);
        imgStand = (ImageView) rootView.findViewById(R.id.imgStand);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        lstComennts = (ListView) rootView.findViewById(R.id.lstComennts);
        btnPost = (Button) rootView.findViewById(R.id.btnPost);
        editComment = (EditText) rootView.findViewById(R.id.editComment);
        scrollPuesto = (ScrollView) rootView.findViewById(R.id.scrollPuesto);
        btnCompartir = (Button) rootView.findViewById(R.id.btnCompartir);
        checkFavorite = (CheckBox) rootView.findViewById(R.id.checkFavorite);
        tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);

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

        checkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
                puesto.setIdUsuario(dataSource.getUsuario());
                if(checkedFavorite==0){
                    new Networking(rootView.getContext()).execute("agregarFavorito",puesto.getIdUsuario(),puesto.getId());
                    checkedFavorite=1;
                    Toast.makeText(getContext(), getActivity().getString(R.string.favoriteAdd), Toast.LENGTH_SHORT).show();
                    checkFavorite.setButtonDrawable(R.mipmap.ic_corazon_rojo);
                }
               else {
                    new Networking(rootView.getContext()).execute("eliminarFavorito",puesto.getIdUsuario(),puesto.getId());
                    checkedFavorite=0;
                    Toast.makeText(getContext(), getActivity().getString(R.string.favoriteDelete), Toast.LENGTH_SHORT).show();
                    checkFavorite.setButtonDrawable(R.mipmap.ic_corazonvacio);
                }

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
                puesto.setIdUsuario(dataSource.getUsuario());
                new Networking(rootView.getContext()).execute("agregarComentario", puesto.getId(), puesto.getIdUsuario(), editComment.getText().toString());
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

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
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
        UsuarioDataSource dataSource = new UsuarioDataSource(getContext());
        int IdUsuario = dataSource.getUsuario();
        new Networking(rootView.getContext()).execute("obtenerFavorito",IdUsuario ,puesto.getId(), new MyCallback() {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkLocationPermission();
        // Instaciammos nuestor objeto mapa
        map = googleMap;

        // Inicializa nuestro objeto LocationManager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Muestra el boton de "Mi Ubicacion" en el mapa (El tipico circulo azul de google)
        map.setMyLocationEnabled(true);

        String Location = puesto.getCoordenadas();

       if (Location.equals("null")) {
           // Ponemos una ubicacion fija
           LatLng mtyLocation = new LatLng(25.65, -100.29);
           CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(mtyLocation, 16.f);
           map.moveCamera(cu);
           MarkerOptions options = new MarkerOptions();
           options.position(mtyLocation);
           options.title(puesto.getNombre());
           map.addMarker(options);

           List<Address> addresses;
           geocoder = new Geocoder(getActivity(), Locale.getDefault());
           try {
               addresses = geocoder.getFromLocation(25.65, -100.29, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
               String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName(); ; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
               //puesto.setDireccion(address);
               tvAddress.setText(address);
           } catch (IOException e) {
               e.printStackTrace();
           }
        }

        else{
            String[] latlong = puesto.getCoordenadas().split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng location = new LatLng(latitude, longitude);
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(location, 16.f);
            // De esta manera se pueden agregar marcadores al mapa
            MarkerOptions options = new MarkerOptions();
            options.position(location);
            options.title(puesto.getNombre());
            map.addMarker(options);

            map.moveCamera(cu);

            tvAddress.setText(puesto.getDireccion());
        }
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

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    FragmentListaPuesto fragmentListaPuesto = new FragmentListaPuesto();
                    fragmentListaPuesto.setIdTipoComida(puesto.getIdTipoComida());
                    changeFragment(fragmentListaPuesto, "listaPuesto");
                    return true;
                }
                return false;
            }
        });
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }

}

