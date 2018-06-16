package moreno.juan.kitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.controlador.RecyclerViewAdaptor;
import moreno.juan.kitch.modelo.Categoria;

public class VisualizarCategoria extends AppCompatActivity {

    public static Categoria categoria_seleccionada;
    LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerViewRecetas;
    private RecyclerViewAdaptor adaptador_recetas;
    private TextView nombre_categoria;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_categoria);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewRecetas = findViewById(R.id.recicler_categorias_recet);
        nombre_categoria = findViewById(R.id.text_categorias_rec);
        recyclerViewRecetas.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRecetas.setLayoutManager(mLayoutManager);

        nombre_categoria.setText(categoria_seleccionada.getNombre_categoria());


        adaptador_recetas = new RecyclerViewAdaptor(categoria_seleccionada.getRecetas());
        recyclerViewRecetas.setAdapter(adaptador_recetas);
        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
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

                        VisualizarReceta.receta_visualizada = categoria_seleccionada.getRecetas().get(position);
                        startActivity(new Intent(getApplicationContext(), VisualizarReceta.class));

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
    }


    public SweetAlertDialog warningMensaje(String titulo, String contexto, String texto_confirmacion) {

        SweetAlertDialog nuevo = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo)
                .setContentText(contexto)
                .setConfirmText(texto_confirmacion);

        return nuevo;
    }

}
