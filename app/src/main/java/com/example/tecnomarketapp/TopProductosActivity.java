package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class TopProductosActivity extends AppCompatActivity {

    TextView tvRankingProductos;
    Button btnVolverTop;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_productos);

        tvRankingProductos = findViewById(R.id.tvRankingProductos);
        btnVolverTop = findViewById(R.id.btnVolverTop);
        db = new DBHelper(this);

        cargarRanking();

        btnVolverTop.setOnClickListener(v -> finish());
    }

    private void cargarRanking() {
        Cursor c = db.top5ProductosVendidos();
        StringBuilder sb = new StringBuilder();
        int puesto = 1;

        while (c.moveToNext()) {
            String nombre = c.getString(0);
            int unidades = c.getInt(1);
            double recaudado = c.getDouble(2);

            sb.append("🏆 Top #").append(puesto).append(": ").append(nombre).append("\n")
                    .append("Unidades vendidas: ").append(unidades).append("\n")
                    .append("Ingresos generados: ").append(String.format(Locale.getDefault(), "S/ %.2f", recaudado)).append("\n")
                    .append("------------------------------------\n");
            puesto++;
        }
        c.close();

        if (puesto == 1) {
            tvRankingProductos.setText("Aún no se han registrado ventas con productos asociados.");
        } else {
            tvRankingProductos.setText(sb.toString());
        }
    }
}