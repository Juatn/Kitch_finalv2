package moreno.juan.kitch.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import moreno.juan.kitch.ModificarReceta;
import moreno.juan.kitch.R;
import moreno.juan.kitch.controlador.RecyclerViewAdaptor;
import moreno.juan.kitch.modelo.Receta;


public class MisRecetasFragment extends Fragment {

    LinearLayoutManager mLayoutManager;
    View rootView;
    FirebaseAuth mAuth;
    private RecyclerView recyclerViewRecetas;
    private RecyclerViewAdaptor adaptador_recetas;
    private List<Receta> misRecetas;
    private Toolbar toolbar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mAuth = FirebaseAuth.getInstance();
        toolbar=rootView.findViewById(R.id.toolbar3);



        toolbar.setTitle("Mis recetas");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        toolbar.setTitleTextColor(Color.parseColor("#ED6C66"));


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        misRecetas = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        rellenarMisRecetas();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        recyclerViewRecetas = view.findViewById(R.id.recicler_mis_recetas);

        recyclerViewRecetas.setLayoutManager(mLayoutManager);
        recyclerViewRecetas.setItemAnimator(new DefaultItemAnimator());
        adaptador_recetas = new RecyclerViewAdaptor(misRecetas);
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
                        ModificarReceta.receta = misRecetas.get(position);
                        startActivity(new Intent(rootView.getContext().getApplicationContext(), ModificarReceta.class));

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

    public void rellenarMisRecetas() {

        if (mAuth.getCurrentUser() != null) {

            for (Receta c : Tab_RecetasFragment.recetas) {
                if (c.getEmail_usuario().equals(mAuth.getCurrentUser().getEmail())) {
                    misRecetas.add(c);
                }
            }
        }
    }
}
