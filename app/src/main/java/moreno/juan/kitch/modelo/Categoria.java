package moreno.juan.kitch.modelo;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private int foto_categoria;
    private String nombre_categoria;
    private List<Receta> recetas;


    public Categoria(int foto_categoria, String nombre_categoria) {
        this.foto_categoria = foto_categoria;
        this.nombre_categoria = nombre_categoria;
        this.recetas = new ArrayList<>();
    }

    public Categoria() {
        this.recetas = new ArrayList<>();
    }

    public int getFoto_categoria() {
        return foto_categoria;
    }

    public void setFoto_categoria(int foto_categoria) {
        this.foto_categoria = foto_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}
