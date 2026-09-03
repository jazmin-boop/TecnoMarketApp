package com.example.tecnomarketapp;

public class ItemSpinner {
    private int id;
    private String nombre;

    public ItemSpinner(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    // Android Studio usa toString() para mostrar el texto visible dentro del Spinner
    @Override
    public String toString() {
        return nombre;
    }
}