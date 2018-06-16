package moreno.juan.kitch;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.controlador.Utils;
import moreno.juan.kitch.modelo.Receta;

public class CrearReceta extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_IMAGE = 101;
    private Spinner spinner_categorias;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private ImageView image_receta;
    private Button btn_new_receta;
    private EditText txt_ingredientes;
    private EditText txt_elaboracion;
    private ProgressBar progressbar_receta;
    private EditText txt_nombre_receta;
    private FirebaseAuth auth;
    private Uri imgURI;
    private StorageTask mUploadTask;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);
        //-------------------------------------------------------------------
        spinner_categorias = findViewById(R.id.spinnerCategorias);
        btn_new_receta = findViewById(R.id.btn_guardar_receta);
        image_receta = findViewById(R.id.imageReceta);
        txt_ingredientes = findViewById(R.id.txtingredientes);
        txt_nombre_receta = findViewById(R.id.txtnombre_receta);
        txt_elaboracion = findViewById(R.id.txtelaboracion);
        progressbar_receta = findViewById(R.id.progressbar_crear_receta);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("recetas");
        storageReference = FirebaseStorage.getInstance().getReference("recetas");
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Utils.categorias);
        //set the spinners adapter to the previously created one.
        spinner_categorias.setAdapter(adapter);

        image_receta.setOnClickListener(this);
        btn_new_receta.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()){

            case R.id.btn_guardar_receta:
                if(camposRellenos()) {
                    guardarReceta();
                }

                break;
            case R.id.imageReceta:
                showImageChooser();
                break;
        }
    }
    private void guardarReceta(){
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Utils.mensajeBasico(
                    this,"En progreso...",SweetAlertDialog.PROGRESS_TYPE)
                    .setCancelText("Salir")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            mUploadTask.cancel();
                        }
                    }).show();
        } else {

            uploadFile();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgURI = data.getData();
            Picasso.get().load(imgURI).into(image_receta);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"), CHOOSE_IMAGE);
    }

    private void uploadFile() {
        if (imgURI != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imgURI));


            mUploadTask = fileReference.putFile(imgURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressbar_receta.setProgress(0);
                                }
                            }, 500);


                            // Se crea una instancia de la clase receta si existe usuario

                            Receta upload = new Receta();
                            upload.setS_nombre(txt_nombre_receta.getText().toString());
                            upload.setS_ingredientes(txt_ingredientes.getText().toString());
                            upload.setCreador_receta(Objects.requireNonNull(auth.getCurrentUser()).getDisplayName());
                            upload.setS_elaboracion(txt_elaboracion.getText().toString());
                            upload.setImg(Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString());
                            upload.setS_categoria(spinner_categorias.getSelectedItem().toString());
                            upload.setEmail_usuario(auth.getCurrentUser().getEmail());

                            // se le genera una id unica
                            //String uploadId = mDatabaseRef.push().getKey();
                            //mDatabaseRef.getKey();
                            upload.setId(mDatabaseRef.push().getKey());

                            // se añade a firebase
                            db.collection(Utils.FIREBASE_BDD_RECETAS)
                                    .add(upload).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Utils.mensajeBasico(getApplicationContext(),"Receta creada",SweetAlertDialog.NORMAL_TYPE).show();
                                }
                            })
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Actualizamos el id de la clase receta con el del documento generado
                                            documentReference.update("id", documentReference.getId());

                                            startActivity(new Intent(getApplicationContext(), Drawler.class));

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressbar_receta.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Sube una foto para la receta", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean camposRellenos() {
        boolean centinela = false;
        String elaboracion = txt_elaboracion.getText().toString().trim();
        String ingredientes = txt_ingredientes.getText().toString().trim();
        String nombreReceta = txt_nombre_receta.getText().toString().trim();
        String nombrecreador = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();

        if (elaboracion.isEmpty()) {
            txt_elaboracion.setError("Define la elaboración de la receta");
            txt_elaboracion.requestFocus();

        } else if (ingredientes.isEmpty()) {
            txt_ingredientes.setError("La receta necesita almenos un ingrediente");
            txt_ingredientes.requestFocus();
        } else if (nombreReceta.isEmpty()) {
            txt_nombre_receta.setError("Ponle un nombre a la receta");
            txt_nombre_receta.requestFocus();
        }
        else {

            centinela = true;
        }
        return centinela;


    }





}
