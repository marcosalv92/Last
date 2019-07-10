package com.example.marcos.last;

/**
 * Created by windows7 on 7/3/2019.
 */
public class ListDatos {
    public String Nombre = "";
    private  boolean Estado=false;
    public String Numero = "";

    public void setNumero(String numero) {
        this.Numero = numero;
    }

    public void setEstado(boolean image) {
        Estado = image;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public boolean getEstado() {
        return Estado;
    }

    public String getNumero() {
        return Numero;
    }
}
