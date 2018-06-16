package moreno.juan.kitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextEmail = findViewById(R.id.editTextEmail);
        logo = findViewById(R.id.logo_app_sign);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
        Picasso.get().load(R.drawable.logo).into(logo);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("El correo esta vacio");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Por favor, ingresa un correo valido");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("La contraseña esta vacia");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Un minimo de 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    mostrarMensaje("Registrado con exito").show();
                    //Toast.makeText(getApplicationContext(),"Registrado con exito",Toast.LENGTH_SHORT).show();

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        warningMensaje("Ha ocurrido un error", "Ya estabas registrado", "Vale").show();


                    } else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public SweetAlertDialog mostrarMensaje(String mensaje) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this)
                .setTitleText(mensaje);
        return nuevo;

    }

    public SweetAlertDialog warningMensaje(String titulo, String contexto, String texto_confirmacion) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(contexto)
                .setConfirmText(texto_confirmacion);

        return nuevo;
    }
}
