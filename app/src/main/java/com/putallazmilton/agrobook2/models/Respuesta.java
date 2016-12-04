package com.putallazmilton.agrobook2.models;

/**
 * Created by Milton on 17/11/2016.
 */

public class Respuesta {
    String usuario;
    String descripcion;
    String idproblema;

    public Respuesta(String usuario, String descripcion) {
        this.usuario = usuario;
        this.descripcion = descripcion;

    }

    public String getIdproblema() {
        return idproblema;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }


}
