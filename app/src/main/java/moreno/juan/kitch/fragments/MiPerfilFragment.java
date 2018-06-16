package moreno.juan.kitch.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.R;

import static android.app.Activity.RESULT_OK;


public class MiPerfilFragment extends Fragment implements View.OnClickListener {
    public static final int CHOOSE_IMAGE = 101;
    FirebaseUser user;
    View view;
    FirebaseFirestore db;
    DocumentReference refReceta;
    private EditText text_nombre_usuario;
    private EditText text_email_usuario;
    private EditText password;
    private ImageView img_usuario;
    private Button btn_edit_nombre;
    private Button btn_cambiar_img;
    private Button btn_edit_correo;
    private Button btn_edit_pass;
    private Uri uriProfileImage;
    private String profileImageUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        // find views
        text_email_usuario = view.findViewById(R.id.editText_correo_usuario_ajustes);
        text_nombre_usuario = view.findViewById(R.id.editText_nombre_usuario_ajustes);
        password = view.findViewById(R.id.editText_password_usuario_ajustes);
        img_usuario = view.findViewById(R.id.imagen_perfil_ajustes);
        btn_edit_nombre = view.findViewById(R.id.btn_cambiar_nombre);
        btn_edit_correo = view.findViewById(R.id.btn_cambiar_email);
        btn_edit_pass = view.findViewById(R.id.btn_cambiar_pass);
        btn_cambiar_img = view.findViewById(R.id.btn_cambiar_foto_perfil);
        db = FirebaseFirestore.getInstance();


        //Listeners de botones
        btn_edit_nombre.setOnClickListener(this);
        btn_edit_pass.setOnClickListener(this);
        btn_edit_correo.setOnClickListener(this);
        img_usuario.setOnClickListener(this);
        btn_cambiar_img.setOnClickListener(this);


        return view;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cambiar_nombre:
                updateNameAndPhoto();
                break;
            case R.id.btn_cambiar_email:
                cambiarEmail();
                break;
            case R.id.btn_cambiar_pass:
                cambiarPass();
                break;
            case R.id.imagen_perfil_ajustes:
                showImageChooser();
                break;
            case R.id.btn_cambiar_foto_perfil:
                cambiarFoto();
        }
    }

    private void cambiarFoto() {

        if (profileImageUrl == null) {
            mensajeError("No hay datos", "La imagen es la misma", "vale").show();
        } else {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()

                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();


            user.updateProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mostrarMensaje("Exito", "Foto perfil actualizada", "ok").show();
                }
            });
        }

    }

    public void cambiarPass() {
        String pass = password.getText().toString();
        if (pass.isEmpty()) {
            password.setError("La contraseña no puede estar vacia");
            password.requestFocus();
        } else {


            user.updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mostrarMensaje("Contraseña", "Su contraseña se ha actualizado", "ok").show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mostrarMensaje("Contraseña", "Su contraseña se ha actualizado", "ok").show();
                }
            });
        }
    }

    public void cambiarEmail() {
        final String email = text_email_usuario.getText().toString();

        if (email.isEmpty()) {
            text_email_usuario.setError("El email esta vacio");
            text_email_usuario.requestFocus();
            return;
        }
        if (email.equals(user.getEmail())) {
            text_email_usuario.setError("El email esta en uso");
            text_email_usuario.requestFocus();
            return;
        }
        if (user != null) {

            user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mostrarMensaje("Correo", "Se ha actualizado el correo", "vale");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mensajeError("Error", "No se ha podido actualizar el correo", "vale").show();
                        }
                    });
        }

    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Saber usuario activo
        user = FirebaseAuth.getInstance().getCurrentUser();
        // cargar datos
        cargarVistaAjustes();


    }

    public void cargarVistaAjustes() {
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            // Cargamos los datos
            Picasso.get().load(photoUrl).into(img_usuario);
            text_nombre_usuario.setText(name);
            text_email_usuario.setText(email);
            password.setText("");
            //Los deshabilitamos
            // text_nombre_usuario.setEnabled(false);
            //text_email_usuario.setEnabled(false);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uriProfileImage);
                img_usuario.setImageBitmap(bitmap);

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

            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una foto de perfil"), CHOOSE_IMAGE);
    }


    public SweetAlertDialog mostrarMensaje(String titulo, String mensaje, String confirmtext) {

        SweetAlertDialog nuevo = new SweetAlertDialog(view.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje).setConfirmText(confirmtext).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });


        return nuevo;

    }

    public SweetAlertDialog mensajeError(String titulo, String mensaje, String cancelText) {

        SweetAlertDialog nuevo = new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });


        return nuevo;

    }

    private void updateNameAndPhoto() {

        //Activamos el campo de nombre
        text_nombre_usuario.setEnabled(true);
        final String nombre_usuario = text_nombre_usuario.getText().toString();

        if (nombre_usuario.isEmpty()) {
            text_nombre_usuario.setError("El nombre esta vacio");
            text_nombre_usuario.requestFocus();
            return;
        } else if (nombre_usuario.equals(user.getDisplayName())) {
            text_nombre_usuario.setError("Actualmente ya te llamas asi.");
            text_nombre_usuario.requestFocus();
            return;
        }

        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nombre_usuario)

                    .build();


            user.updateProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mostrarMensaje("Nombre actualizado", "Su nick se ha actualizado ha " + nombre_usuario + " correctamente.",
                            "vale").show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            task.isSuccessful();
                        }
                    });


        }


    }
}

