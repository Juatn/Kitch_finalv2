package moreno.juan.kitch;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.fragments.CestaCompraFragment;
import moreno.juan.kitch.fragments.GalleryFragment;
import moreno.juan.kitch.fragments.MiPerfilFragment;
import moreno.juan.kitch.fragments.MisRecetasFragment;

public class Drawler extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseUser user;
    private TextView nameuser;
    private TextView emailuser;
    private ImageView userImg;
    private FragmentManager fragmentManager;

    public static Drawable getDrawable(Context mContext, String name) {
        int resourceId = mContext.getResources().getIdentifier(name, "Kitch", mContext.getPackageName());
        return mContext.getResources().getDrawable(resourceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawler);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearReceta.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new GalleryFragment()).commit();

        user = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //cerramos sesion
            FirebaseAuth.getInstance().signOut();
            finish();
            //iniciamos ventana login
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        nameuser = findViewById(R.id.txt_nombre_ususario);
        emailuser = findViewById(R.id.txt_email_usuario);
        userImg = findViewById(R.id.imagen_perfil);
        cargaUsuario();

        FragmentManager fragmentManager = getSupportFragmentManager();


        switch (id) {
            case R.id.nav_camera:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new CestaCompraFragment()).commit();
                break;
            case R.id.nav_gallery:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new GalleryFragment()).commit();

                break;
            case R.id.nav_slideshow:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new MisRecetasFragment()).commit();
                break;
            case R.id.nav_manage:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new MiPerfilFragment()).commit();
                break;
            case R.id.nav_share:

                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargaUsuario() {
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            Picasso.get().load(photoUrl).into(userImg);

            nameuser.setText(name);
            emailuser.setText(email);
        }
    }

    public SweetAlertDialog mostrarMensaje(String mensaje) {

        SweetAlertDialog nuevo = new SweetAlertDialog(getApplicationContext())
                .setTitleText(mensaje);
        return nuevo;

    }


}
