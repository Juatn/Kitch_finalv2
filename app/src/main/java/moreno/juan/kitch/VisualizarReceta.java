package moreno.juan.kitch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.controlador.RecyclerViewComentarios;
import moreno.juan.kitch.controlador.Utils;
import moreno.juan.kitch.modelo.Comentario;
import moreno.juan.kitch.modelo.Ingrediente;
import moreno.juan.kitch.modelo.Receta;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class VisualizarReceta extends AppCompatActivity implements View.OnClickListener {

    public static List<Comentario> listComentarios;
    public static Receta receta_visualizada;
    LinearLayoutManager mLayoutManager;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private RatingBar puntuacion_receta; // visualizada
    private TextView nombre_receta;
    private ImageView foto_receta_vista;
    private TextView nombre_autor_receta;
    private TextView elaboracion_receta;
    private TextView ingredientes_receta;
    private TextView categoria_receta;
    private EditText escribir_nuevo_comentario;
    private RatingBar puntuacion_receta_comentario;
    private Button btn_enviar_comentario;
    private FirebaseAuth auth;
    private RecyclerView recycler_comentarios;
    private RecyclerViewComentarios adaptador_comentarios;
    private Button btn_cesta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_receta);

        //
        puntuacion_receta = findViewById(R.id.rating_receta_vista);
        puntuacion_receta.setEnabled(false);
        categoria_receta = findViewById(R.id.txt_categoria_receta_vista);
        nombre_receta = findViewById(R.id.txt_nombre_receta_vista);
        nombre_autor_receta = findViewById(R.id.txt_nombre_autor_receta_vista);
        elaboracion_receta = findViewById(R.id.txt_elaboracion_receta_vista);
        ingredientes_receta = findViewById(R.id.txt_ingredientes_receta_vista);
        escribir_nuevo_comentario = findViewById(R.id.txt_nuevo_comentario);
        foto_receta_vista = findViewById(R.id.imagen_foto_receta_vista);
        puntuacion_receta_comentario = findViewById(R.id.rating_puntuacion_nuevareceta);
        btn_enviar_comentario = findViewById(R.id.btn_enviar_comentario);
        mAuth = FirebaseAuth.getInstance();


        btn_cesta = findViewById(R.id.btn_cesta);


        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        if (receta_visualizada != null) {
            db.collection(Utils.FIREBASE_BDD_RECETAS).document(receta_visualizada.getId()).collection(Utils.FIREBASE_BDD_COMENTARIOS).orderBy("fecha_publicacion", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //listComentarios.removeAll(listComentarios);

                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    // convert document to POJO


                                    Comentario comentario = document.toObject(Comentario.class);


                                    listComentarios.add(comentario);

                                    float nota = 0f;
                                    for (Comentario c : listComentarios) {
                                        if(c.getF_nota_receta()>=0) {

                                            nota += c.getF_nota_receta();
                                        }
                                    }

                                    float notafinal = nota / listComentarios.size();
                                    receta_visualizada.setPuntuacion(notafinal);
                                    db.collection(Utils.FIREBASE_BDD_RECETAS).document(receta_visualizada.getId()).update("puntuacion", notafinal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                    puntuacion_receta.setRating(notafinal);


                                }

                            } else {
                                Log.w(TAG, "Error al recoger datos.", task.getException());
                            }
                        }
                    });

        }


        if (receta_visualizada != null) {
            cargarReceta();
        }
        listComentarios = new ArrayList<>();


        btn_enviar_comentario.setOnClickListener(this);
        btn_cesta.setOnClickListener(this);


        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        recycler_comentarios = findViewById(R.id.recicler_comentarios);
        recycler_comentarios.setItemAnimator(new DefaultItemAnimator());
        recycler_comentarios.setLayoutManager(mLayoutManager);

        adaptador_comentarios = new RecyclerViewComentarios(listComentarios);
        recycler_comentarios.setAdapter(adaptador_comentarios);


        //


        foto_receta_vista.requestFocus();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enviar_comentario:
                enviarComentario();
                break;
            case R.id.btn_cesta:
                cargarCesta();
                break;


        }
    }


    public void cargarCesta() {
        Ingrediente cesta = new Ingrediente();
        cesta.setEmail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        cesta.setIngredientes(ingredientes_receta.getText().toString());
        cesta.setNombre_receta(nombre_receta.getText().toString());
        db.collection(Utils.FIREBASE_BDD_CESTA).add(cesta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                documentReference.update("id", documentReference.getId());
                mostrarMensaje("Ingredientes añadidos a la cesta", "Su lista de ingredientes se ha actualizado", "vale").show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                warningMensaje("Opss...", "Algo salio mal", "vale").show();
            }
        });


    }

    public void enviarComentario() {


        if (escribir_nuevo_comentario.getText().toString().isEmpty()) {
            escribir_nuevo_comentario.setError("El comentario no puede estar vacio");
            escribir_nuevo_comentario.requestFocus();
        } else {
            Comentario mensaje = new Comentario();


            mensaje.setNombre_usuario(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
            mensaje.setF_nota_receta(puntuacion_receta_comentario.getRating());
            mensaje.setS_mensaje(escribir_nuevo_comentario.getText().toString());
            mensaje.setImagen_usuario(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString());

            // Get a new write batch
             {
                FirebaseFirestore.getInstance().collection(Utils.FIREBASE_BDD_RECETAS)
                        .document(receta_visualizada.getId()).collection(Utils.FIREBASE_BDD_COMENTARIOS).add(mensaje).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mostrarMensaje("Enviado", "Comentario publicado", "vale").show();
                        escribir_nuevo_comentario.setText("");
                        puntuacion_receta_comentario.setRating(0f);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }

        }

    }


    public void cargarReceta() {

        // puntuacion de la receta
        if (receta_visualizada.getPuntuacion() != 0) {
            puntuacion_receta.setRating(receta_visualizada.getPuntuacion());
        }
        //nombre de la receta
        nombre_receta.setText(receta_visualizada.getS_nombre());
        // nombre autor de la receta
        nombre_autor_receta.setText(receta_visualizada.getCreador_receta());
        // la elaboracion
        elaboracion_receta.setText(receta_visualizada.getS_elaboracion());
        // ingredientes
        ingredientes_receta.setText(receta_visualizada.getS_ingredientes());
        categoria_receta.setText("Categoria => " + receta_visualizada.getS_categoria());
        // ponemos la foto
        Picasso.get().load(receta_visualizada.getImg()).into(foto_receta_vista);

    }

    public SweetAlertDialog mensajeSimple(String texto){

        return new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                .setContentText(texto);
    }


    public SweetAlertDialog mostrarMensaje(String titulo, String mensaje, String confirmtext) {


        return new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmText(confirmtext);

    }


    public SweetAlertDialog warningMensaje(String titulo, String contexto, String texto_confirmacion) {

        return new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(contexto)
                .setConfirmText(texto_confirmacion);
    }
}
