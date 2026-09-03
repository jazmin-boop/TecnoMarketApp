package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class TopVentasActivity extends AppCompatActivity {

    TextView tvRankingVentas;
    Button btnVolverTop;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ventas);

        tvRankingVentas = findViewById(R.id.tvRankingVentas);
        btnVolverTop = findViewById(R.id.btnVolverTop);
        db = new DBHelper(this);

        cargarRanking();

        btnVolverTop.setOnClickListener(v -> finish());
    }

    private void cargarRanking() {
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT v.id_venta, (c.nombres || ' ' || c.apellidos) AS cliente, v.total, v.fecha " +
                        "FROM ventas v " +
                        "INNER JOIN clientes c ON v.id_cliente = c.id_cliente " +
                        "ORDER BY v.total DESC LIMIT 5", null);

        StringBuilder sb = new StringBuilder();
        int puesto = 1;

        while (c.moveToNext()) {
            int idVenta = c.getInt(0);
            String cliente = c.getString(1);
            double total = c.getDouble(2);
            String fecha = c.getString(3);

            sb.append("🏆 Top #").append(puesto).append(": Comprobante #").append(idVenta).append("\n")
                    .append("Cliente: ").append(cliente).append("\n")
                    .append("Fecha de emisión: ").append(fecha).append("\n")
                    .append("Total facturado: ").append(String.format(Locale.getDefault(), "S/ %.2f", total)).append("\n")
                    .append("------------------------------------\n");
            puesto++;
        }
        c.close();

        if (puesto == 1) {
            tvRankingVentas.setText("Aún no se han registrado transacciones de ventas.");
        } else {
            tvRankingVentas.setText(sb.toString());
        }
    }
}