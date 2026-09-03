package com.example.tecnomarketapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class DetalleProductoActivity extends AppCompatActivity {

    ImageView ivDetalleImagen;
    TextView tvDetalleNombre, tvDetalleCodigo, tvDetallePrecio,
            tvDetalleCategoria, tvDetalleStock, tvDetalleDescripcion;
    Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        ivDetalleImagen = findViewById(R.id.ivDetalleImagen);
        tvDetalleNombre = findViewById(R.id.tvDetalleNombre);
        tvDetalleCodigo = findViewById(R.id.tvDetalleCodigo);
        tvDetallePrecio = findViewById(R.id.tvDetallePrecio);
        tvDetalleCategoria = findViewById(R.id.tvDetalleCategoria);
        tvDetalleStock = findViewById(R.id.tvDetalleStock);
        tvDetalleDescripcion = findViewById(R.id.tvDetalleDescripcion);
        btnVolver = findViewById(R.id.btnVolver);

        Producto p = (Producto) getIntent().getSerializableExtra("producto");

        if (p != null) {
            tvDetalleNombre.setText(p.getNombre());
            tvDetalleCodigo.setText("Código: " + p.getCodigo());
            tvDetallePrecio.setText(String.format(Locale.getDefault(), "S/ %.2f", p.getPrecio()));
            tvDetalleCategoria.setText("Categoría: " + p.getCategoria());
            tvDetalleStock.setText("Stock: " + p.getStock());
            tvDetalleDescripcion.setText(p.getDescripcion().isEmpty() ? "Sin descripción" : p.getDescripcion());

            // Carga dinámica de la imagen desde drawable según el nombre registrado
            String nombreImg = p.getImagen();
            int resId = 0;
            if (nombreImg != null && !nombreImg.trim().isEmpty()) {
                resId = getResources().getIdentifier(nombreImg.trim(), "drawable", getPackageName());
            }
            if (resId != 0) {
                ivDetalleImagen.setImageResource(resId);
            } else {
                ivDetalleImagen.setImageResource(R.drawable.tecnomarket_logo); // Imagen por defecto
            }
        }

        btnVolver.setOnClickListener(v -> finish());
    }
}