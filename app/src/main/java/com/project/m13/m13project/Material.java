package com.project.m13.m13project;

import android.graphics.Bitmap;

/**
 * Created by alumne on 15/04/16.
 */
public class Material {
    int id;
    String id_usu;
    String nombre;
    String texto;
    String categoria;
    String propietario;
    double precio;
    byte[] foto;

    public Material(int id, String id_usu, String nombre, String texto, String categoria, double precio, byte[] foto, String propietario) {
        this.id = id;
        this.id_usu = id_usu;
        this.nombre = nombre;
        this.texto = texto;
        this.categoria = categoria;
        this.precio = precio;
        this.foto = foto;
        this.propietario=propietario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_usu() {
        return id_usu;
    }

    public void setId_usu(String id_usu) {
        this.id_usu = id_usu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
}
