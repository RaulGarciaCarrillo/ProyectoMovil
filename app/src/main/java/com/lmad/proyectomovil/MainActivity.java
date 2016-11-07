package com.lmad.proyectomovil;

import android.content.pm.ActivityInfo;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.lmad.proyectomovil.Fragments.FragmentListaFavoritos;
import com.lmad.proyectomovil.Fragments.FragmentLogin;
import com.lmad.proyectomovil.Fragments.FragmentMenuPrincipal;
import com.lmad.proyectomovil.Fragments.FragmentPerfil;
import com.lmad.proyectomovil.database.UsuarioDataSource;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DrawerLocker, GestureOverlayView.OnGesturePerformedListener {
    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private GestureLibrary libreria;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!libreria.load()){
            //finish();
        }
        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);
        gestureOverlayView.addOnGesturePerformedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        navigationView = (NavigationView) findViewById(R.id.nav_viewer);
        android.app.Fragment existingFragment = getFragmentManager().findFragmentById(android.R.id.content);

        if (existingFragment == null) {
            changeFragment(new FragmentLogin(), "login");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navInicio) {
                    changeFragment(new FragmentMenuPrincipal(), "inicio");
                } else if (id == R.id.navPerfil) {
                    changeFragment(new FragmentPerfil(), "perfil");
                } else if (id == R.id.navFavoritos) {
                    changeFragment(new FragmentListaFavoritos(), "favoritos");
                } else if (id == R.id.navCerrarSesion) {
                    UsuarioDataSource dataSource = new UsuarioDataSource(MainActivity.this);
                    dataSource.deleteUsuario();
                    changeFragment(new FragmentLogin(), "login");
                }
                //cerrar el navigation
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment currentFragment = fm.findFragmentByTag(tag);

        if (currentFragment != null && currentFragment.isVisible()){
            Toast.makeText(this, getResources().getString((R.string.toast_Fragment)), Toast.LENGTH_SHORT).show();
            return;
        }

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction ft = fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)
            //ft.addToBackStack(null); //no regresar al último fragmento
            ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)
            ft.commit();//cerrar conexión
        }
    }

    @Override
    public void setDrawerEnable(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED:
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
    }

    @Override
    public void RefreshNav() {
        invalidateOptionsMenu();
        //Toast.makeText(this, "Refrescar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = libreria.recognize(gesture);
        if (predictions.size() > 0 && predictions.get(0).score > 1) {
            String action = predictions.get(0).name;
            Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
