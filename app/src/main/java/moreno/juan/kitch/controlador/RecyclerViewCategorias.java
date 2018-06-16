package moreno.juan.kitch.controlador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import moreno.juan.kitch.R;
import moreno.juan.kitch.modelo.Categoria;

public class RecyclerViewCategorias extends RecyclerView.Adapter<RecyclerViewCategorias.ViewMensaje> {


    public static List<Categoria> categorias;
    private LayoutInflater mInflater;


    public RecyclerViewCategorias(Context context, List<Categoria> categorias) {

        RecyclerViewCategorias.categorias = categorias;
        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias, parent, false);


        return new ViewMensaje(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMensaje holder, int position) {


        Categoria nuevo = categorias.get(position);

        holder.nombre_categoria.setText(nuevo.getNombre_categoria());


        Picasso.get().load(nuevo.getFoto_categoria()).resize(250, 250).centerCrop()
                .into(holder.foto);

    }


    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class ViewMensaje extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nombre_categoria;
        private ImageView foto;


        public ViewMensaje(View itemView) {
            super(itemView);



            nombre_categoria = itemView.findViewById(R.id.txt_nombre_categoria);
            foto = itemView.findViewById(R.id.img_categoria_item);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    }

    // Convenience method for getting data at click position
    public Categoria getItem(int id) {
        return categorias.get(id);
    }

    // Method that executes your code for the action received
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position);
    }
}







