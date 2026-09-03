package com.example.tecnomarketapp;

public class Venta {
    private int idVenta;
    private String fecha;
    private String cliente;
    private String vendedor;
    private double subtotal;
    private double igv;
    private double total;

    public Venta(int idVenta, String fecha, String cliente, String vendedor, double subtotal, double igv, double total) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    public int getIdVenta() { return idVenta; }
    public String getFecha() { return fecha; }
    public String getCliente() { return cliente; }
    public String getVendedor() { return vendedor; }
    public double getSubtotal() { return subtotal; }
    public double getIgv() { return igv; }
    public double getTotal() { return total; }
}