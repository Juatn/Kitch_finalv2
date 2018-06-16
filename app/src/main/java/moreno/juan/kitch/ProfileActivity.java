package moreno.juan.kitch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.controlador.Utils;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CHOOSE_IMAGE = 101;
    private static final String TAG = ProfileActivity.class.getSimpleName();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textView;
    ImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private Button btn_guardar;
    boolean verificado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = findViewById(R.id.textViewVerified);
        editText = findViewById(R.id.editTextDisplayName);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        btn_guardar=findViewById(R.id.buttonSave);
        user = mAuth.getCurrentUser();


        imageView.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarUsuario();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonSave:
                if(verificado){
                    guardarUsuario();
                }
                else{
                    enviarVerificacion();
                }
                break;
            case R.id.imageView:
                showImageChooser();
                break;
        }
    }
    public void enviarVerificacion(){
        user.sendEmailVerification();
                Utils.mensajeBasico(ProfileActivity.this,"Correo verificación enviado", SweetAlertDialog.SUCCESS_TYPE).show();

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (user.getDisplayName() != null && user.getPhotoUrl() != null&& verificado) {
            saltarVentana();

        }
    }


    private void cargarUsuario() {



        if (user != null) {

            if (user.getPhotoUrl() != null) {
                Picasso.get()
                        .load(user.getPhotoUrl())
                        .into(imageView);

            }

            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Correo verificado");
                textView.setTextColor(Color.GREEN);
                btn_guardar.setText("Guardar nombre y foto usuario");
                verificado = true;
            }
            if(!user.isEmailVerified()){
                textView.setText("Verifica el correo para continuar, Tambien debe proporcionar un nombre de usuario y una foto de perfil");
                textView.setTextColor(Color.RED);
            }

        }

    }


    public void saltarVentana() {

        Intent intent = new Intent(ProfileActivity.this, Drawler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageUrl = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void guardarUsuario(){
        String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Nombre requerido");
            editText.requestFocus();
            return;
        }

        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();


            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        if(user.isEmailVerified()){
                            saltarVentana();
                        }
                    }
                }
            });

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

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto de perfil"), CHOOSE_IMAGE);
    }


}