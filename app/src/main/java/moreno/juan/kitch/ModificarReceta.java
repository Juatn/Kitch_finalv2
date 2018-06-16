package moreno.juan.kitch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.controlador.Utils;
import moreno.juan.kitch.modelo.Receta;

public class ModificarReceta extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner_categorias;
    private static final int CHOOSE_IMAGE = 101;
    private ImageView image_receta;
    private Button btn_new_receta;
    private EditText txt_ingredientes;
    private EditText txt_elaboracion;
    private ProgressBar progressbar_receta;
    private Button btn_eliminar;
    private EditText txt_nombre_receta;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    DocumentReference recetasRef;
    public static Receta receta;
    private Uri imgURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_receta);

        spinner_categorias = findViewById(R.id.spinnerCategorias_mod);
        btn_new_receta = findViewById(R.id.btn_guardar_receta_mod);
        image_receta = findViewById(R.id.imageReceta_mod);
        txt_ingredientes = findViewById(R.id.txtingredientes_mod);
        txt_nombre_receta = findViewById(R.id.txtnombre_receta_mod);
        txt_elaboracion = findViewById(R.id.txtelaboracion_mod);
        progressbar_receta = findViewById(R.id.progressbar_crear_receta_mod);
        btn_eliminar= findViewById(R.id.btn_eliminar_receta);
        db = FirebaseFirestore.getInstance();
        recetasRef= db.collection(Utils.FIREBASE_BDD_RECETAS).document(receta.getId());
        auth = FirebaseAuth.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Utils.categorias);
        //set the spinners adapter to the previously created one.
        spinner_categorias.setAdapter(adapter);



        //image_receta.setOnClickListener(this);
        btn_new_receta.setOnClickListener(this);
        btn_eliminar.setOnClickListener(this);
        cargarReceta();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_guardar_receta_mod:
                updateReceta();
                break;
            case R.id.btn_eliminar_receta:
                eliminarReceta();
                break;

        }

    }

    public void cargarReceta() {

        if(receta!=null){
            //cargamos imagen
            Picasso.get().load(receta.getImg()).into(image_receta);

            txt_elaboracion.setText(receta.getS_elaboracion());
            txt_ingredientes.setText(receta.getS_ingredientes());
            txt_nombre_receta.setText(receta.getS_nombre());


        }

    }

    public void updateReceta(){


        recetasRef.update(
                "s_elaboracion",txt_elaboracion.getText().toString(),
                "s_ingredientes",txt_ingredientes.getText().toString(),
                "s_nombre",txt_nombre_receta.getText().toString(),
                "s_categoria",spinner_categorias.getSelectedItem().toString()
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mensajeConfirmacion(""+receta.getS_nombre(),"Se ha actualizado","vale").show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void eliminarReceta(){
        recetasRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensajeConfirmacion(receta.getS_nombre(),"Ha sido eliminada","vale").show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensajeConfirmacion(receta.getS_nombre(),"No se ha podido eliminar","vale").show();
            }
        });
    }
    public SweetAlertDialog mostrarMensaje(String titulo, String mensaje, String confirmtext) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmText(confirmtext);


        return nuevo;

    }
    public SweetAlertDialog mensajeError(String titulo, String mensaje, String confirmtext) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmText(confirmtext)
                ;



        return nuevo;

    }

    public SweetAlertDialog mensajeConfirmacion(String titulo, String mensaje, String confirmtext) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmText(confirmtext)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(new Intent(getApplicationContext(),Drawler.class));
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })

                ;



        return nuevo;


    }




}
