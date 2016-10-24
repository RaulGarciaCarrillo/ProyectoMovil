package com.lmad.proyectomovil;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.lmad.proyectomovil.Fragments.FragmentListaFavoritos;
import com.lmad.proyectomovil.Fragments.FragmentLogin;
import com.lmad.proyectomovil.Fragments.FragmentMenuPrincipal;
import com.lmad.proyectomovil.Fragments.FragmentPerfil;


public class MainActivity extends AppCompatActivity implements DrawerLocker{

    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewer);

        changeFragment(new FragmentMenuPrincipal(),"menu");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if (id==R.id.navInicio){
                    changeFragment(new FragmentMenuPrincipal(),"inicio");
                } else if (id==R.id.navPerfil){
                    changeFragment(new FragmentPerfil(),"perfil");
                } else if(id==R.id.navFavoritos){
                    changeFragment(new FragmentListaFavoritos(),"favoritos");
                } else if(id==R.id.navCerrarSesion){
                    changeFragment(new FragmentLogin(),"login");
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
            Toast.makeText(this, "¡Ya está abierto!", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentTransaction ft= fm.beginTransaction(); //abrir una transicion (agregar, quitar o reemplazar)

        //ft.addToBackStack(null); //no regresar al último fragmento
        ft.replace(R.id.frame_container, fragment, tag); //(id, fragmento)

        ft.commit();//cerrar conexión
    }

    @Override
    public void setDrawerEnable(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED:
                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        //toogle.setDrawerI
    }
}
