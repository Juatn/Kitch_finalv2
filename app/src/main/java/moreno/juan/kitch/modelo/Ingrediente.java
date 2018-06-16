package moreno.juan.kitch.modelo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ingrediente {
   // Atributos
    private String ingredientes;
    private String nombre_receta;
    private String email;   // FOREIGN KEY
    private String id;
    private String fecha_creacion;


    public Ingrediente() {
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        this.fecha_creacion=sdf.format(date);
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getNombre_receta() {
        return nombre_receta;
    }

    public void setNombre_receta(String nombre_receta) {
        this.nombre_receta = nombre_receta;
    }

    @Override
    public String toString() {
        /*El sub-string es para sacar solo la el dia de creación, pero me interesa tener la fecha completa guardada
          para la ordenacion en BDD
        */
        return nombre_receta +"\n"+
                ingredientes+"\n"+ "Fecha creación -->"+fecha_creacion.substring(0,10)+"\n";
    }
}
