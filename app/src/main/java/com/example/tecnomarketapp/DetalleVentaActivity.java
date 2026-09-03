package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class DetalleVentaActivity extends AppCompatActivity {

    ImageView ivDetalleVentaImagen;
    TextView tvDetalleVentaProducto, tvDetalleVentaIdVenta, tvDetalleVentaIdDetalle;
    TextView tvDetalleVentaImporte, tvDetalleVentaCantidad, tvDetalleVentaPrecioUnit, tvDetalleVentaInfo;
    Button btnVolverVentas;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);

        ivDetalleVentaImagen = findViewById(R.id.ivDetalleVentaImagen);
        tvDetalleVentaProducto = findViewById(R.id.tvDetalleVentaProducto);
        tvDetalleVentaIdVenta = findViewById(R.id.tvDetalleVentaIdVenta);
        tvDetalleVentaIdDetalle = findViewById(R.id.tvDetalleVentaIdDetalle);
        tvDetalleVentaImporte = findViewById(R.id.tvDetalleVentaImporte);
        tvDetalleVentaCantidad = findViewById(R.id.tvDetalleVentaCantidad);
        tvDetalleVentaPrecioUnit = findViewById(R.id.tvDetalleVentaPrecioUnit);
        tvDetalleVentaInfo = findViewById(R.id.tvDetalleVentaInfo);
        btnVolverVentas = findViewById(R.id.btnVolverVentas);

        db = new DBHelper(this);

        int idVenta = getIntent().getIntExtra("idVenta", 1);
        cargarDatosDetalle(idVenta);

        btnVolverVentas.setOnClickListener(v -> finish());
    }

    private void cargarDatosDetalle(int idVenta) {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT d.id_detalle, p.nombre, d.cantidad, d.precio_unitario, d.importe, p.imagen " +
                        "FROM detalle_venta d " +
                        "INNER JOIN productos p ON d.id_producto = p.id_producto " +
                        "WHERE d.id_venta = ? LIMIT 1",
                new String[]{String.valueOf(idVenta)});

        if (c.moveToFirst()) {
            int idDetalle = c.getInt(0);
            String nombreProd = c.getString(1);
            int cantidad = c.getInt(2);
            double precioUnit = c.getDouble(3);
            double importe = c.getDouble(4);
            String imagen = c.getString(5);

            tvDetalleVentaProducto.setText(nombreProd);
            tvDetalleVentaIdVenta.setText("Comprobante #" + idVenta);
            tvDetalleVentaIdDetalle.setText("Ítem #" + idDetalle);
            tvDetalleVentaImporte.setText(String.format(Locale.US, "S/ %.2f", importe));
            tvDetalleVentaCantidad.setText(cantidad + " unid.");
            tvDetalleVentaPrecioUnit.setText(String.format(Locale.US, "S/ %.2f", precioUnit));
            tvDetalleVentaInfo.setText("Transacción procesada correctamente. Stock descontado del almacén central.");

            if (imagen != null && !imagen.isEmpty()) {
                int resId = getResources().getIdentifier(imagen, "drawable", getPackageName());
                ivDetalleVentaImagen.setImageResource(resId != 0 ? resId : R.drawable.tecnomarket_logo);
            } else {
                ivDetalleVentaImagen.setImageResource(R.drawable.tecnomarket_logo);
            }
        } else {
            tvDetalleVentaProducto.setText("Sin ítems registrados");
            tvDetalleVentaIdVenta.setText("Comprobante #" + idVenta);
            tvDetalleVentaIdDetalle.setText("Ítem #0");
            tvDetalleVentaImporte.setText("S/ 0.00");
            tvDetalleVentaCantidad.setText("0 unid.");
            tvDetalleVentaPrecioUnit.setText("S/ 0.00");
            tvDetalleVentaInfo.setText("Esta venta aún no cuenta con detalles agregados.");
            ivDetalleVentaImagen.setImageResource(R.drawable.tecnomarket_logo);
        }
        c.close();
    }
}