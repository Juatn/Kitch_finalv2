package moreno.juan.kitch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.R;
import moreno.juan.kitch.VisualizarReceta;
import moreno.juan.kitch.controlador.RecyclerViewAdaptor;
import moreno.juan.kitch.controlador.Utils;
import moreno.juan.kitch.modelo.Comentario;
import moreno.juan.kitch.modelo.Receta;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class Tab_RecetasFragment extends Fragment {

    public static List<Receta> recetas = new ArrayList<>();
    public List<Comentario> comentarios;
    LinearLayoutManager mLayoutManager;
    View rootView;
    FirebaseFirestore db;
    Receta nueva;
    private RecyclerView recyclerViewRecetas;
    private RecyclerViewAdaptor adaptador_recetas;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // recyclerViewRecetas=(RecyclerView)container.findViewById(R.id.recyclerRecetas);
        // recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(getContext()));

        //adaptador_recetas= new RecyclerViewAdaptor(obtenerRecetas());

        rootView = inflater.inflate(R.layout.fragment_recetas, container, false);
        rootView.setTag(TAG);


        return inflater.inflate(R.layout.fragment_recetas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        comentarios = new ArrayList<>();

        recyclerViewRecetas = view.findViewById(R.id.recyclerRecetas);

        recyclerViewRecetas.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecetas.setLayoutManager(mLayoutManager);

        adaptador_recetas = new RecyclerViewAdaptor(recetas);
        recyclerViewRecetas.setAdapter(adaptador_recetas);

        final GestureDetector mGestureDetector = new GestureDetector(rootView.getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerViewRecetas.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int position = recyclerView.getChildAdapterPosition(child);
                        // puede petar try catch
                        VisualizarReceta.receta_visualizada = recetas.get(position);

                        startActivity(new Intent(rootView.getContext().getApplicationContext(), VisualizarReceta.class));

                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });


        db = FirebaseFirestore.getInstance();

        db.collection("recetas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            recetas.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                // convert document to POJO

                                nueva = document.toObject(Receta.class);

                                recetas.add(nueva);


                            }

                        } else {
                            Log.w(TAG, "Error al recoger datos.", task.getException());
                        }
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();


    }


    public void actualizaComentarios() {

        db.collection(Utils.FIREBASE_BDD_RECETAS).document(nueva.getId()).collection(Utils.FIREBASE_BDD_COMENTARIOS)
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


                                comentarios.add(comentario);

                                float nota = 0f;
                                for (Comentario c : comentarios) {
                                    if(c.getF_nota_receta()>=0){

                                        nota += c.getF_nota_receta();
                                    }
                                }
                                float notafinal = nota / comentarios.size();
                                nueva.setPuntuacion(notafinal);
                                db.collection(Utils.FIREBASE_BDD_RECETAS).document().update("puntuacion", notafinal);


                            }

                        } else {
                            Log.w(TAG, "Error al recoger datos.", task.getException());
                        }
                    }
                });

    }

    public SweetAlertDialog warningMensaje(String titulo, String contexto, String texto_confirmacion) {

        SweetAlertDialog nuevo = new SweetAlertDialog(rootView.getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo)
                .setContentText(contexto)
                .setConfirmText(texto_confirmacion);

        return nuevo;
    }

}




