package moreno.juan.kitch.controlador;

import android.content.Context;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import moreno.juan.kitch.modelo.Receta;

public class Utils {


    public static ArrayList<Receta>recetas = new ArrayList<>();
    // PARA LA BDD FIREBASE
    public static final String FIREBASE_BDD_RECETAS="recetas";
    public static final String FIREBASE_BDD_COMENTARIOS="comentarios";
    public static final String FIREBASE_BDD_CESTA="CESTA";

    // CATEGORIAS DE COMIDA
    public static String[] categorias={"Bebidas","Carnes","Ensaladas","Pescado y mariscos","Arroces y Pasta","Postres"
    };



    //GESTIONAR DIFERENTES MENSAJES

    //Mensaje basico , con un simple texto
    public static SweetAlertDialog mensajeBasico(Context context, String mensaje, int tipoMensaje){

        SweetAlertDialog nuevo=new SweetAlertDialog(context,tipoMensaje)
                .setContentText(mensaje);
                return  nuevo;
    }






}
