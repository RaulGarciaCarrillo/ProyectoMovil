package com.lmad.proyectomovil.model;

import android.graphics.Bitmap;

/**
 * Created by Raul on 29/10/2016.
 */

public class Puesto {
    int id;
    int idUsuario;
    String nombre;
    String descripcion;
    String direccion;
    String coordenadas;
    String foto;

    int idTipoComida;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoComida() {
        return idTipoComida;
    }

    public void setIdTipoComida(int idTipoComida) {
        this.idTipoComida = idTipoComida;
    }
}
