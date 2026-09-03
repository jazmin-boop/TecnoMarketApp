package com.example.tecnomarketapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvBienvenida, tvRol, tvTotalProductos, tvTotalClientes, tvTotalVentas;
    View cardProductos, cardCategorias, cardClientes, cardUsuarios;
    View btnIrTopProductos, btnIrTopVentas, btnIrReporteStock;
    Button btnNuevaVentaModulo, btnCerrarSesion;
    DBHelper db;
    int idUsuario;
    String nombreUsuario, rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vistas de Métricas Superiores
        tvBienvenida = findViewById(R.id.tvBienvenida);
        tvRol = findViewById(R.id.tvRol);
        tvTotalProductos = findViewById(R.id.tvTotalProductos);
        tvTotalClientes = findViewById(R.id.tvTotalClientes);
        tvTotalVentas = findViewById(R.id.tvTotalVentas);

        // Módulos CRUD
        cardProductos = findViewById(R.id.cardProductos);
        cardCategorias = findViewById(R.id.cardCategorias);
        cardClientes = findViewById(R.id.cardClientes);
        cardUsuarios = findViewById(R.id.cardUsuarios);

        // Botón Directo: Registro de Ventas
        btnNuevaVentaModulo = findViewById(R.id.btnNuevaVentaModulo);

        // Reportes y Estadísticas (Sección Inferior)
        btnIrTopProductos = findViewById(R.id.btnIrTopProductos);
        btnIrTopVentas = findViewById(R.id.btnIrTopVentas);
        btnIrReporteStock = findViewById(R.id.btnIrReporteStock);

        // Cierre de Sesión
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        db = new DBHelper(this);

        // Parámetros de sesión
        idUsuario = getIntent().getIntExtra("idUsuario", 0);
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        rol = getIntent().getStringExtra("rol");

        tvBienvenida.setText("Hola, " + (nombreUsuario != null ? nombreUsuario : "Admin"));
        tvRol.setText("Rol: " + (rol != null ? rol.toUpperCase() : "ADMINISTRADOR"));

        // Validación de permisos para módulo Usuarios
        boolean esAdmin = rol == null || "ADMIN".equalsIgnoreCase(rol.trim()) || "ADMINISTRADOR".equalsIgnoreCase(rol.trim());
        if (cardUsuarios != null) {
            if (!esAdmin) {
                cardUsuarios.setEnabled(false);
                cardUsuarios.setAlpha(0.35f);
            } else {
                cardUsuarios.setEnabled(true);
                cardUsuarios.setAlpha(1.0f);
            }

            cardUsuarios.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, UsuariosActivity.class));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Verifica UsuariosActivity en el AndroidManifest.xml", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Navegación Módulos CRUD
        if (cardProductos != null) {
            cardProductos.setOnClickListener(v -> startActivity(new Intent(this, ProductosActivity.class)));
        }
        if (cardCategorias != null) {
            cardCategorias.setOnClickListener(v -> startActivity(new Intent(this, CategoriasActivity.class)));
        }
        if (cardClientes != null) {
            cardClientes.setOnClickListener(v -> startActivity(new Intent(this, ClientesActivity.class)));
        }

        // Acción Rápida: Gestión y Registro de Ventas (Abre la pantalla de tu captura)
        if (btnNuevaVentaModulo != null) {
            btnNuevaVentaModulo.setOnClickListener(v -> {
                Intent i = new Intent(this, VentasActivity.class);
                i.putExtra("idUsuario", idUsuario);
                startActivity(i);
            });
        }

        // SECCIÓN REPORTES Y ESTADÍSTICAS
        // 1. Top 5 Productos Más Vendidos
        if (btnIrTopProductos != null) {
            btnIrTopProductos.setOnClickListener(v ->
                    startActivity(new Intent(this, TopProductosActivity.class))
            );
        }

        // 2. Top 5 Mayores Ventas (Abre el reporte de recaudación)
        if (btnIrTopVentas != null) {
            btnIrTopVentas.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(MainActivity.this, TopVentasActivity.class));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Verifica TopVentasActivity en el AndroidManifest.xml", Toast.LENGTH_LONG).show();
                }
            });
        }

        // 3. Alertas de Stock Bajo
        if (btnIrReporteStock != null) {
            btnIrReporteStock.setOnClickListener(v ->
                    startActivity(new Intent(this, ReporteStockActivity.class))
            );
        }

        // Cerrar Sesión
        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMetricas();
    }

    private void cargarMetricas() {
        if (tvTotalProductos != null) tvTotalProductos.setText(String.valueOf(db.contarProductos()));
        if (tvTotalClientes != null) tvTotalClientes.setText(String.valueOf(db.contarClientes()));
        if (tvTotalVentas != null) tvTotalVentas.setText(String.valueOf(db.contarVentas()));
    }
}