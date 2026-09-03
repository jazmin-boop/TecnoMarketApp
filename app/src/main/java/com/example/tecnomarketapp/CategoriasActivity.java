package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CategoriasActivity extends AppCompatActivity {

    EditText etId, etNombre, etDescripcion, etBuscar;
    Button btnGuardar, btnActualizar, btnEliminar, btnListar, btnBuscar;
    TextView tvResultado;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        etId = findViewById(R.id.etId);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etBuscar = findViewById(R.id.etBuscar);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListar = findViewById(R.id.btnListar);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvResultado = findViewById(R.id.tvResultado);

        db = new DBHelper(this);

        // Búsqueda en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGuardar.setOnClickListener(v -> guardar());
        btnActualizar.setOnClickListener(v -> actualizar());
        btnEliminar.setOnClickListener(v -> eliminar());
        btnListar.setOnClickListener(v -> listar(db.mostrarCategorias()));
        btnBuscar.setOnClickListener(v -> buscar());

        listar(db.mostrarCategorias());
    }

    private boolean validar(boolean conId) {
        if (conId && etId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese ID de la categoría", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre de la categoría", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validar(false)) return;
        long r = db.insertarCategoria(
                etNombre.getText().toString().trim(),
                etDescripcion.getText().toString().trim(),
                1
        );
        Toast.makeText(this, r != -1 ? "Categoría guardada" : "Error o nombre duplicado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        listar(db.mostrarCategorias());
    }

    private void actualizar() {
        if (!validar(true)) return;
        int r = db.actualizarCategoria(
                Integer.parseInt(etId.getText().toString().trim()),
                etNombre.getText().toString().trim(),
                etDescripcion.getText().toString().trim(),
                1
        );
        Toast.makeText(this, r > 0 ? "Categoría actualizada" : "ID no encontrado", Toast.LENGTH_SHORT).show();
        listar(db.mostrarCategorias());
    }

    private void eliminar() {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Categoría")
                .setMessage("¿Deseas eliminar permanentemente esta categoría?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    int r = db.eliminarCategoria(Integer.parseInt(idStr));
                    if (r > 0) {
                        Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        listar(db.mostrarCategorias());
                    } else {
                        Toast.makeText(this, "No se encontró el ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void buscar() {
        String texto = etBuscar.getText().toString().trim();
        if (texto.isEmpty()) {
            listar(db.mostrarCategorias());
            return;
        }
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM categorias WHERE (nombre LIKE ? OR id_categoria = ?) AND estado = 1",
                new String[]{"%" + texto + "%", texto});
        listar(c);
    }

    private void listar(Cursor c) {
        StringBuilder sb = new StringBuilder();
        if (c.getCount() == 0) {
            tvResultado.setText("No hay categorías registradas.");
            c.close();
            return;
        }
        while (c.moveToNext()) {
            sb.append("🏷️ ID: ").append(c.getInt(0))
                    .append(" | ").append(c.getString(1)).append("\n")
                    .append("📝 Descrip: ").append(c.getString(2) != null ? c.getString(2) : "-").append("\n")
                    .append("--------------------------------------------------\n");
        }
        c.close();
        tvResultado.setText(sb.toString());
    }

    private void limpiarCampos() {
        etId.setText("");
        etNombre.setText("");
        etDescripcion.setText("");
    }
}