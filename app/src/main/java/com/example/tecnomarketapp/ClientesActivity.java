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

public class ClientesActivity extends AppCompatActivity {

    EditText etId, etDni, etNombres, etApellidos, etTelefono, etCorreo, etDireccion, etBuscar;
    Button btnGuardar, btnActualizar, btnEliminar, btnListar, btnBuscar;
    TextView tvResultado;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        etId = findViewById(R.id.etId);
        etDni = findViewById(R.id.etDni);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etDireccion = findViewById(R.id.etDireccion);
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
        btnListar.setOnClickListener(v -> listar(db.mostrarClientes()));
        btnBuscar.setOnClickListener(v -> buscar());

        listar(db.mostrarClientes());
    }

    private boolean validar(boolean conId) {
        if (conId && etId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDni.getText().toString().trim().isEmpty() ||
                etNombres.getText().toString().trim().isEmpty() ||
                etApellidos.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios (DNI, Nombres, Apellidos)", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validar(false)) return;
        long r = db.insertarCliente(
                etDni.getText().toString().trim(),
                etNombres.getText().toString().trim(),
                etApellidos.getText().toString().trim(),
                etTelefono.getText().toString().trim(),
                etCorreo.getText().toString().trim(),
                etDireccion.getText().toString().trim(),
                1
        );
        Toast.makeText(this, r != -1 ? "Cliente registrado" : "Error o DNI duplicado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        listar(db.mostrarClientes());
    }

    private void actualizar() {
        if (!validar(true)) return;
        int r = db.actualizarCliente(
                Integer.parseInt(etId.getText().toString().trim()),
                etDni.getText().toString().trim(),
                etNombres.getText().toString().trim(),
                etApellidos.getText().toString().trim(),
                etTelefono.getText().toString().trim(),
                etCorreo.getText().toString().trim(),
                etDireccion.getText().toString().trim(),
                1
        );
        Toast.makeText(this, r > 0 ? "Cliente actualizado" : "ID no encontrado", Toast.LENGTH_SHORT).show();
        listar(db.mostrarClientes());
    }

    private void eliminar() {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Cliente")
                .setMessage("¿Deseas eliminar permanentemente a este cliente?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    int r = db.eliminarCliente(Integer.parseInt(idStr));
                    if (r > 0) {
                        Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        listar(db.mostrarClientes());
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
            listar(db.mostrarClientes());
            return;
        }
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM clientes WHERE (dni LIKE ? OR apellidos LIKE ? OR nombres LIKE ?) AND estado = 1",
                new String[]{"%" + texto + "%", "%" + texto + "%", "%" + texto + "%"});
        listar(c);
    }

    private void listar(Cursor c) {
        StringBuilder sb = new StringBuilder();
        if (c.getCount() == 0) {
            tvResultado.setText("No se encontraron clientes.");
            c.close();
            return;
        }
        while (c.moveToNext()) {
            sb.append("👤 ID: ").append(c.getInt(0))
                    .append(" | DNI: ").append(c.getString(1)).append("\n")
                    .append("   Nombre: ").append(c.getString(3)).append(", ").append(c.getString(2)).append("\n")
                    .append("   Telf: ").append(c.getString(4) != null ? c.getString(4) : "-")
                    .append(" | ").append(c.getString(5) != null ? c.getString(5) : "-").append("\n")
                    .append("   Dir: ").append(c.getString(6) != null ? c.getString(6) : "-").append("\n")
                    .append("--------------------------------------------------\n");
        }
        c.close();
        tvResultado.setText(sb.toString());
    }

    private void limpiarCampos() {
        etId.setText("");
        etDni.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etTelefono.setText("");
        etCorreo.setText("");
        etDireccion.setText("");
    }
}