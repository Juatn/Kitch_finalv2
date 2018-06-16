package moreno.juan.kitch.modelo;

import java.util.ArrayList;

/**
 * Created by juana on 29/03/2018.
 */

public class Usuario {

    private String s_id_usuario;
    private String s_nombre;
    private String s_correo;
    private ArrayList<Comentario>comentarios;

    public Usuario(){

    }

    public String getS_id_usuario() {
        return s_id_usuario;
    }

    public void setS_id_usuario(String s_id_usuario) {
        this.s_id_usuario = s_id_usuario;
    }

    public String getS_nombre() {
        return s_nombre;
    }

    public void setS_nombre(String s_nombre) {
        this.s_nombre = s_nombre;
    }



    public String getS_correo() {
        return s_correo;
    }

    public void setS_correo(String s_correo) {
        this.s_correo = s_correo;
    }
}
