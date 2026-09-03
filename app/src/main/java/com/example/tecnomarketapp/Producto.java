package com.example.tecnomarketapp;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private String descripcion;
    private double precio;
    private int stock;
    private String imagen;

    public Producto(int id, String codigo, String nombre, String categoria,
                    String descripcion, double precio, int stock, String imagen) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getImagen() { return imagen; }
}