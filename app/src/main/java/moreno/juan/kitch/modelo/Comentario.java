package moreno.juan.kitch.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by juana on 19/04/2018.
 */

public class Comentario {

    private String nombre_usuario;
    private String imagen_usuario;
    private String s_mensaje;
    private float f_nota_receta;
    private String fecha_publicacion;
    private SimpleDateFormat sdf;

    public Comentario() {

        Date date = new Date();

        sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        fecha_publicacion = sdf.format(date);
    }

    public String getImagen_usuario() {
        return imagen_usuario;
    }

    public void setImagen_usuario(String imagen_usuario) {
        this.imagen_usuario = imagen_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getS_mensaje() {
        return s_mensaje;
    }

    public void setS_mensaje(String s_mensaje) {
        this.s_mensaje = s_mensaje;
    }

    public float getF_nota_receta() {
        return f_nota_receta;
    }

    public void setF_nota_receta(float f_nota_receta) {
        this.f_nota_receta = f_nota_receta;
    }


    public String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(String fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }
}

