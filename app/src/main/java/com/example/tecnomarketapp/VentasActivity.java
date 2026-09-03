package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasActivity extends AppCompatActivity {

    EditText etIdVenta, etSubtotal, etIgv, etTotal, etBuscar;
    Spinner spCliente;
    Button btnRegistrar, btnActualizar, btnEliminar, btnListar, btnBuscar;
    RecyclerView rvVentas;
    VentaAdapter adapter;
    List<Venta> listaVentas;
    List<ItemSpinner> listaClientes; // REUTILIZA TU ItemSpinner
    DBHelper db;
    int idUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        etIdVenta = findViewById(R.id.etIdVenta);
        spCliente = findViewById(R.id.spCliente);
        etSubtotal = findViewById(R.id.etSubtotal);
        etIgv = findViewById(R.id.etIgv);
        etTotal = findViewById(R.id.etTotal);
        etBuscar = findViewById(R.id.etBuscar);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListar = findViewById(R.id.btnListar);
        btnBuscar = findViewById(R.id.btnBuscar);

        db = new DBHelper(this);
        idUsuarioActual = getIntent().getIntExtra("idUsuario", 1);

        // RecyclerView Setup (Igual a Productos)
        rvVentas = findViewById(R.id.rvVentas);
        rvVentas.setLayoutManager(new LinearLayoutManager(this));
        listaVentas = new ArrayList<>();
        adapter = new VentaAdapter(listaVentas);
        rvVentas.setAdapter(adapter);

        cargarClientesSpinner();

        // Auto-cálculo de IGV y Total
        etSubtotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().trim().isEmpty()) {
                        double sub = Double.parseDouble(s.toString().trim());
                        double igv = sub * 0.18;
                        double tot = sub + igv;
                        etIgv.setText(String.format(Locale.US, "%.2f", igv));
                        etTotal.setText(String.format(Locale.US, "%.2f", tot));
                    }
                } catch (Exception ignored) {}
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

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

        btnRegistrar.setOnClickListener(v -> registrar());
        btnActualizar.setOnClickListener(v -> actualizar());
        btnEliminar.setOnClickListener(v -> eliminar());
        btnListar.setOnClickListener(v -> listar(db.mostrarVentas()));
        btnBuscar.setOnClickListener(v -> buscar());

        listar(db.mostrarVentas());
    }

    private void cargarClientesSpinner() {
        listaClientes = new ArrayList<>();
        Cursor c = db.obtenerClientesSpinner();
        while (c.moveToNext()) {
            listaClientes.add(new ItemSpinner(c.getInt(0), c.getString(1)));
        }
        c.close();

        ArrayAdapter<ItemSpinner> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, listaClientes);
        spCliente.setAdapter(spinnerAdapter);
    }

    private boolean validar(boolean conId) {
        if (conId && etIdVenta.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese ID de la venta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (listaClientes.isEmpty()) {
            Toast.makeText(this, "Debe registrar clientes primero", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etSubtotal.getText().toString().trim().isEmpty() || etTotal.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Complete los montos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registrar() {
        if (!validar(false)) return;

        ItemSpinner cli = (ItemSpinner) spCliente.getSelectedItem();
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        long r = db.insertarVenta(
                cli.getId(),
                idUsuarioActual,
                fechaActual,
                Double.parseDouble(etSubtotal.getText().toString().trim()),
                Double.parseDouble(etIgv.getText().toString().trim()),
                Double.parseDouble(etTotal.getText().toString().trim())
        );

        Toast.makeText(this, r != -1 ? "Venta registrada con éxito" : "Error al guardar", Toast.LENGTH_SHORT).show();
        limpiarCampos();
        listar(db.mostrarVentas());
    }

    private void actualizar() {
        if (!validar(true)) return;

        ItemSpinner cli = (ItemSpinner) spCliente.getSelectedItem();
        int r = db.actualizarVenta(
                Integer.parseInt(etIdVenta.getText().toString().trim()),
                cli.getId(),
                Double.parseDouble(etSubtotal.getText().toString().trim()),
                Double.parseDouble(etIgv.getText().toString().trim()),
                Double.parseDouble(etTotal.getText().toString().trim())
        );

        Toast.makeText(this, r > 0 ? "Venta actualizada" : "ID no encontrado", Toast.LENGTH_SHORT).show();
        listar(db.mostrarVentas());
    }

    private void eliminar() {
        String idStr = etIdVenta.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la venta", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Venta")
                .setMessage("¿Deseas eliminar permanentemente esta venta?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    int r = db.eliminarVenta(Integer.parseInt(idStr));
                    if (r > 0) {
                        Toast.makeText(this, "Venta eliminada", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        listar(db.mostrarVentas());
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
            listar(db.mostrarVentas());
            return;
        }
        Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT v.id_venta, v.fecha, (c.nombres || ' ' || c.apellidos) AS cliente, " +
                        "u.nombre AS vendedor, v.subtotal, v.igv, v.total " +
                        "FROM ventas v " +
                        "INNER JOIN clientes c ON v.id_cliente=c.id_cliente " +
                        "INNER JOIN usuarios u ON v.id_usuario=u.id_usuario " +
                        "WHERE c.nombres LIKE ? OR c.apellidos LIKE ? OR v.id_venta = ? " +
                        "ORDER BY v.id_venta DESC",
                new String[]{"%" + texto + "%", "%" + texto + "%", texto});
        listar(c);
    }

    private void listar(Cursor c) {
        listaVentas.clear();
        while (c.moveToNext()) {
            listaVentas.add(new Venta(
                    c.getInt(0),      // id_venta
                    c.getString(1),   // fecha
                    c.getString(2),   // cliente
                    c.getString(3),   // vendedor
                    c.getDouble(4),   // subtotal
                    c.getDouble(5),   // igv
                    c.getDouble(6)    // total
            ));
        }
        c.close();
        adapter.actualizarLista(listaVentas);
    }

    private void limpiarCampos() {
        etIdVenta.setText("");
        etSubtotal.setText("");
        etIgv.setText("");
        etTotal.setText("");
    }
}