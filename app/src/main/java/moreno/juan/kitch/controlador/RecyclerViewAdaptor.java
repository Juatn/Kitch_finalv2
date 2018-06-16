package moreno.juan.kitch.controlador;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moreno.juan.kitch.R;
import moreno.juan.kitch.modelo.Receta;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre_receta;
        private TextView nombre_creador;
        private RelativeLayout layout;
        ImageView foto_receta;
        RatingBar puntos_receta;

        public ViewHolder(View itemView) {
            super(itemView);

            nombre_receta = itemView.findViewById(R.id.tvNombreReceta);
            foto_receta = itemView.findViewById(R.id.imgReceta);
            puntos_receta = itemView.findViewById(R.id.ratingReceta);
            layout = itemView.findViewById(R.id.layout_item_receta);
            nombre_creador = itemView.findViewById(R.id.txtnombre_creador_receta);


        }


    }

    public static List<Receta> recetario;

    public RecyclerViewAdaptor(List<Receta> recetario) {

        RecyclerViewAdaptor.recetario = recetario;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receta, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Receta nueva = recetario.get(position);



        holder.nombre_receta.setText(nueva.getS_nombre());
        // foto
        Picasso.get().load(nueva.getImg()).into(holder.foto_receta);
        holder.puntos_receta.setRating(nueva.getPuntuacion());
        holder.puntos_receta.setEnabled(false);
        holder.nombre_creador.setText("Cocinero: " + nueva.getCreador_receta());



        switch (nueva.getS_categoria()) {
            case "Bebidas":
                holder.layout.setBackgroundColor(Color.parseColor("#D8C6DE"));
                break;
            case "Carnes":
                holder.layout.setBackgroundColor(Color.parseColor("#E8C277"));
                break;
            case "Ensaladas":
                holder.layout.setBackgroundColor(Color.parseColor("#C5D084"));
                break;
            case "Pescado y mariscos":
                holder.layout.setBackgroundColor(Color.parseColor("#D2DEF6"));
                break;
            case "Arroces y Pasta":
                holder.layout.setBackgroundColor(Color.parseColor("#F9BBB0"));
                break;
            case "Postres":
                holder.layout.setBackgroundColor(Color.parseColor("#FFF9CF"));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return recetario.size();
    }

}

