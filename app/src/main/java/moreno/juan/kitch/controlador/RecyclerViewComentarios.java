package moreno.juan.kitch.controlador;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import moreno.juan.kitch.R;
import moreno.juan.kitch.modelo.Comentario;

public class RecyclerViewComentarios extends RecyclerView.Adapter<RecyclerViewComentarios.ViewMensaje> {


    public static List<Comentario> comentarios;

    public RecyclerViewComentarios(List<Comentario> coments) {

        comentarios = coments;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public RecyclerViewComentarios.ViewMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        RecyclerViewComentarios.ViewMensaje holder = new ViewMensaje(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMensaje holder, int position) {


        Comentario nuevo = comentarios.get(position);

        holder.autor_comentario.setText(nuevo.getNombre_usuario());
        holder.mensaje.setText(nuevo.getS_mensaje());
        holder.nota.setRating(nuevo.getF_nota_receta());
        holder.fecha_publicacion.setText(nuevo.getFecha_publicacion());
        holder.nota.setEnabled(false);

    }


    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ViewMensaje extends RecyclerView.ViewHolder {

        private TextView autor_comentario;
        private TextView mensaje;
        private RatingBar nota;
        private TextView fecha_publicacion;



        public ViewMensaje(View itemView) {
            super(itemView);

            nota = itemView.findViewById(R.id.rating_nota_comentario);
            autor_comentario = itemView.findViewById(R.id.txt_nombre_autor_comentario);
            mensaje = itemView.findViewById(R.id.txt_mensj_creado);
            fecha_publicacion = itemView.findViewById(R.id.txt_fecha_publicacion);




        }


    }
}
