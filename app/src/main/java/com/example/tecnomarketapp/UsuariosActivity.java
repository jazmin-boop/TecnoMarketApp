package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UsuariosActivity extends AppCompatActivity {

    EditText etId, etNombre, etUsuario, etCorreo, etPassword, etBuscar;
    Spinner spRol;
    Button btnGuardar, btnActualizar, btnEliminar, btnListar, btnBuscar;
    TextView tvResultado;
    DBHelper db;
    String[] roles = {"ADMIN", "VENDEDOR", "CAJERO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        etId = findViewById(R.id.etId);
        etNombre = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etBuscar = findViewById(R.id.etBuscar);
        spRol = findViewById(R.id.spRol);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListar = findViewById(R.id.btnListar);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvResultado = findViewById(R.id.tvResultado);

        db = new DBHelper(this);

        // Cargar Roles en el Spinner
        ArrayAdapter<String> adapterRol = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spRol.setAdapter(adapterRol);

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
        btnListar.setOnClickListener(v -> listar(db.mostrarUsuarios()));
        btnBuscar.setOnClickListener(v -> buscar());

        listar(db.mostrarUsuarios());
    }

    private boolean validar(boolean conId) {
        if (conId && etId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etNombre.getText().toString().trim().isEmpty() ||
                etUsuario.getText().toString().trim().isEmpty() ||
                etCorreo.getText().toString().trim().isEmpty() ||
                etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Complete todos los campos del usuario", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validar(false)) return;

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        long r = db.insertarUsuario(
                etNombre.getText().toString().trim(),
                etUsuario.getText().toString().trim(),
                etCorreo.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                spRol.getSelectedItem().toString(),
                1,
                fechaActual
        );

        Toast.makeText(this, r != -1 ? "Usuario registrado con éxito" : "Error o usuario/correo duplicado", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        listar(db.mostrarUsuarios());
    }

    private void actualizar() {
        if (!validar(true)) return;

        int r = db.actualizarUsuario(
                Integer.parseInt(etId.getText().toString().trim()),
                etNombre.getText().toString().trim(),
                etUsuario.getText().toString().trim(),
                etCorreo.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                spRol.getSelectedItem().toString(),
                1
        );

        Toast.makeText(this, r > 0 ? "Usuario actualizado" : "ID no encontrado", Toast.LENGTH_SHORT).show();
        listar(db.mostrarUsuarios());
    }

    private void eliminar() {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del usuario a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        if (id == 1) {
            Toast.makeText(this, "No se puede eliminar la cuenta de Administrador principal", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Deseas dar de baja permanentemente a este usuario?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    int r = db.eliminarUsuario(id);
                    if (r > 0) {
                        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        listar(db.mostrarUsuarios());
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
            listar(db.mostrarUsuarios());
            return;
        }
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT * FROM usuarios WHERE (usuario LIKE ? OR nombre LIKE ? OR correo LIKE ? OR id_usuario = ?) AND estado = 1",
                new String[]{"%" + texto + "%", "%" + texto + "%", "%" + texto + "%", texto});
        listar(c);
    }

    private void listar(Cursor c) {
        StringBuilder sb = new StringBuilder();
        if (c.getCount() == 0) {
            tvResultado.setText("No se encontraron usuarios.");
            c.close();
            return;
        }
        while (c.moveToNext()) {
            sb.append("🔐 ID: ").append(c.getInt(0))
                    .append(" | Rol: ").append(c.getString(5)).append("\n")
                    .append("   Nombre: ").append(c.getString(1)).append("\n")
                    .append("   User: @").append(c.getString(2))
                    .append(" | ").append(c.getString(3)).append("\n")
                    .append("--------------------------------------------------\n");
        }
        c.close();
        tvResultado.setText(sb.toString());
    }

    private void limpiarCampos() {
        etId.setText("");
        etNombre.setText("");
        etUsuario.setText("");
        etCorreo.setText("");
        etPassword.setText("");
    }
}