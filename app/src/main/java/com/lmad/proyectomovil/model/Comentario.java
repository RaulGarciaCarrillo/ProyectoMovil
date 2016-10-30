package com.lmad.proyectomovil.model;

/**
 * Created by Raul on 30/10/2016.
 */

public class Comentario {
    String comentario;
    Usuario usuario;
    Puesto puesto;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }
}
