package com.putallazmilton.agrobook2.models;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by Milton on 10/11/2016.
 */

public class Problema implements Serializable{


    String usuario;
    String descripcion;
    String id;
    String idusuario;

    public Problema(String descripcion, String usuario, String id,String idusuario)  {

        this.descripcion = descripcion;
        this.usuario = usuario;
        this.id = id;
        this.idusuario=idusuario;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public Problema(){

        }


    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
