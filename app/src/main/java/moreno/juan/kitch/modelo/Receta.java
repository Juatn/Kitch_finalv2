package moreno.juan.kitch.modelo;

/**
 * Created by juana on 29/03/2018.
 */

public class Receta {

    private String img;
    private String s_nombre;
    private String s_categoria;
    private String s_elaboracion;
    private String s_ingredientes;
    private boolean b_favorita = false;
    private float puntuacion;
    private String creador_receta;
    private String id;
    private String email_usuario;


    public Receta(String img, String nombre, String categoria, String elaboracion, String ingredientes, String creador_receta, String id) {

        this.s_nombre = nombre;
        this.img = img;
        this.s_categoria = categoria;
        this.s_elaboracion = elaboracion;
        this.s_ingredientes = ingredientes;
        this.creador_receta = creador_receta;
        this.id = id;


    }

    public Receta() {


    }


    public String getCreador_receta() {
        return creador_receta;
    }

    public void setCreador_receta(String creador_receta) {
        this.creador_receta = creador_receta;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getS_nombre() {
        return s_nombre;
    }

    public void setS_nombre(String s_nombre) {
        this.s_nombre = s_nombre;
    }

    public String getS_categoria() {
        return s_categoria;
    }

    public void setS_categoria(String s_categoria) {
        this.s_categoria = s_categoria;
    }

    public String getS_elaboracion() {
        return s_elaboracion;
    }

    public void setS_elaboracion(String s_elaboracion) {
        this.s_elaboracion = s_elaboracion;
    }

    public String getS_ingredientes() {
        return s_ingredientes;
    }

    public void setS_ingredientes(String s_ingredientes) {
        this.s_ingredientes = s_ingredientes;
    }

    public boolean isB_favorita() {
        return b_favorita;
    }

    public void setB_favorita(boolean b_favorita) {
        this.b_favorita = b_favorita;
    }


    @Override
    public String toString() {
        return "Receta{" +
                "img=" + img +
                ", s_nombre='" + s_nombre + '\'' +
                ", s_categoria='" + s_categoria + '\'' +
                ", s_elaboracion='" + s_elaboracion + '\'' +
                ", s_ingredientes='" + s_ingredientes + '\'' +
                ", b_favorita=" + b_favorita +
                ", puntuacion=" + puntuacion +
                ", creador_receta='" + creador_receta + '\'' +

                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }
}
