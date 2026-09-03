package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReporteStockActivity extends AppCompatActivity {

    EditText etLimiteStock;
    Button btnFiltrarStock, btnVolverStock;
    TextView tvReporteStock;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_stock);

        etLimiteStock = findViewById(R.id.etLimiteStock);
        btnFiltrarStock = findViewById(R.id.btnFiltrarStock);
        btnVolverStock = findViewById(R.id.btnVolverStock);
        tvReporteStock = findViewById(R.id.tvReporteStock);

        db = new DBHelper(this);

        btnFiltrarStock.setOnClickListener(v -> cargarReporte());
        btnVolverStock.setOnClickListener(v -> finish());

        cargarReporte(); // Carga inicial por defecto con límite 5
    }

    private void cargarReporte() {
        String limiteStr = etLimiteStock.getText().toString().trim();
        if (limiteStr.isEmpty()) {
            Toast.makeText(this, "Ingrese un límite de unidades", Toast.LENGTH_SHORT).show();
            return;
        }

        int limite = Integer.parseInt(limiteStr);
        Cursor c = db.productosStockBajo(limite);
        StringBuilder sb = new StringBuilder();

        int contador = 0;
        while (c.moveToNext()) {
            contador++;
            String codigo = c.getString(0);
            String nombre = c.getString(1);
            int stock = c.getInt(2);

            sb.append("⚠️ Producto #").append(contador).append("\n")
                    .append("Código: ").append(codigo).append("\n")
                    .append("Nombre: ").append(nombre).append("\n")
                    .append("Stock Actual: ").append(stock).append(" unid.\n")
                    .append("------------------------------------\n");
        }
        c.close();

        if (contador == 0) {
            tvReporteStock.setText("✅ No hay productos con stock menor o igual a " + limite + " unidades.");
        } else {
            tvReporteStock.setText(sb.toString());
        }
    }
}