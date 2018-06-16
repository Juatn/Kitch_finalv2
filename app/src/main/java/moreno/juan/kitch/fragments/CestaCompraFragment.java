package moreno.juan.kitch.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.R;
import moreno.juan.kitch.controlador.Utils;
import moreno.juan.kitch.modelo.Ingrediente;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class CestaCompraFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    View view;
    private TextView cesta_compra;
    private Button btn_eliminar;
    private List<Ingrediente> ingredientes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate layout
        view = inflater.inflate(R.layout.fragment_cesta, container, false);

        cesta_compra = view.findViewById(R.id.txt_cesta_ingredientes);
        cesta_compra.setText("");
        ingredientes = new ArrayList<>();
        btn_eliminar = view.findViewById(R.id.btn_eliminar_cesta);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensajeConfirmacion("Cesta compra", "¿Esta seguro de que desea borrar la cesta?", "SI", "NO").show();
            }
        });


        db.collection(Utils.FIREBASE_BDD_CESTA).whereEqualTo("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //listComentarios.removeAll(listComentarios);

                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        // convert document to POJO
                        Ingrediente comentario = document.toObject(Ingrediente.class);
                        ingredientes.add(comentario);
                        cesta_compra.setText(ingredientes.toString());
                    }

                } else {
                    Log.w(TAG, "Error al recoger datos.", task.getException());
                }
            }
        });
    }

    public SweetAlertDialog mensajeConfirmacion(String titulo, final String mensaje, String confirmtextSI, String confirmtextNO) {

        SweetAlertDialog nuevo = new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .setConfirmText(confirmtextSI)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (!ingredientes.isEmpty()) {
                            for (Ingrediente c : ingredientes) {
                                db.collection(Utils.FIREBASE_BDD_CESTA).document(c.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        warningMensaje("Eliminada", "Cesta eliminada con exito!").show();
                                    }
                                });
                            }
                        } else {
                            sDialog.dismissWithAnimation();
                        }
                    }
                }).setCancelText(confirmtextNO).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        return nuevo;
    }

    public SweetAlertDialog warningMensaje(String titulo, String contexto) {

        SweetAlertDialog nuevo = new SweetAlertDialog(view.getContext().getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(contexto);
        return nuevo;
    }
}
