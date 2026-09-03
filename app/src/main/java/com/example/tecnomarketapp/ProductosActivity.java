package com.example.tecnomarketapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    EditText etId, etCodigo, etNombre, etDescripcion, etPrecio, etStock, etImagen, etBuscar;
    Spinner spCategoria;
    Button btnGuardar, btnActualizar, btnEliminar, btnListar, btnBuscar;
    RecyclerView rvProductos;
    ProductoAdapter adapter;
    List<Producto> listaProductos;
    List<ItemSpinner> listaCategorias;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        etId = findViewById(R.id.etId);
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        spCategoria = findViewById(R.id.spCategoria);
        etPrecio = findViewById(R.id.etPrecio);
        etStock = findViewById(R.id.etStock);
        etImagen = findViewById(R.id.etImagen);
        etBuscar = findViewById(R.id.etBuscar);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnListar = findViewById(R.id.btnListar);
        btnBuscar = findViewById(R.id.btnBuscar);
        // Búsqueda en tiempo real conforme el usuario escribe
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No requiere acción previa
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = s.toString().trim();
                if (texto.isEmpty()) {
                    listar(db.mostrarProductos()); // Si borra todo, muestra todos los productos
                } else {
                    listar(db.buscarProductos(texto)); // Filtra en SQLite en tiempo real
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No requiere acción posterior
            }
        });

        // Configuración de RecyclerView
        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        listaProductos = new ArrayList<>();
        adapter = new ProductoAdapter(listaProductos);
        rvProductos.setAdapter(adapter);

        db = new DBHelper(this);

        // Cargar Categorías en el Spinner desde SQLite
        cargarCategoriasSpinner();

        btnGuardar.setOnClickListener(v -> guardar());
        btnActualizar.setOnClickListener(v -> actualizar());
        btnEliminar.setOnClickListener(v -> eliminar());
        btnListar.setOnClickListener(v -> listar(db.mostrarProductos()));
        btnBuscar.setOnClickListener(v -> buscar());

        listar(db.mostrarProductos());
    }

    private void cargarCategoriasSpinner() {
        listaCategorias = new ArrayList<>();
        Cursor c = db.obtenerCategoriasSpinner();
        while (c.moveToNext()) {
            listaCategorias.add(new ItemSpinner(c.getInt(0), c.getString(1)));
        }
        c.close();

        ArrayAdapter<ItemSpinner> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, listaCategorias);
        spCategoria.setAdapter(spinnerAdapter);
    }

    private boolean validar(boolean conId) {
        if (conId && etId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (listaCategorias.isEmpty()) {
            Toast.makeText(this, "Debe registrar primero al menos una categoría", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etCodigo.getText().toString().trim().isEmpty() ||
                etNombre.getText().toString().trim().isEmpty() ||
                etPrecio.getText().toString().trim().isEmpty() ||
                etStock.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        double precio = Double.parseDouble(etPrecio.getText().toString());
        int stock = Integer.parseInt(etStock.getText().toString());
        if (precio <= 0 || stock < 0) {
            Toast.makeText(this, "Precio > 0 y stock >= 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardar() {
        if (!validar(false)) return;

        // Se obtiene el objeto seleccionado directamente del Spinner
        ItemSpinner catSeleccionada = (ItemSpinner) spCategoria.getSelectedItem();
        int idCategoria = catSeleccionada.getId();

        long r = db.insertarProducto(
                etCodigo.getText().toString().trim(),
                etNombre.getText().toString().trim(),
                etDescripcion.getText().toString().trim(),
                idCategoria,
                Double.parseDouble(etPrecio.getText().toString()),
                Integer.parseInt(etStock.getText().toString()),
                etImagen.getText().toString().trim(), 1);

        Toast.makeText(this, r != -1 ? "Producto guardado" : "Error al guardar", Toast.LENGTH_SHORT).show();
        listar(db.mostrarProductos());
    }

    private void actualizar() {
        if (!validar(true)) return;

        ItemSpinner catSeleccionada = (ItemSpinner) spCategoria.getSelectedItem();
        int idCategoria = catSeleccionada.getId();

        int r = db.actualizarProducto(
                Integer.parseInt(etId.getText().toString()),
                etCodigo.getText().toString().trim(),
                etNombre.getText().toString().trim(),
                etDescripcion.getText().toString().trim(),
                idCategoria,
                Double.parseDouble(etPrecio.getText().toString()),
                Integer.parseInt(etStock.getText().toString()),
                etImagen.getText().toString().trim(), 1);

        Toast.makeText(this, r > 0 ? "Producto actualizado" : "ID no encontrado", Toast.LENGTH_SHORT).show();
        listar(db.mostrarProductos());
    }

    private void eliminar() {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del producto a eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que deseas eliminar permanentemente este producto?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    int id = Integer.parseInt(idStr);
                    int r = db.eliminarProducto(id);
                    if (r > 0) {
                        Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        listar(db.mostrarProductos());
                    } else {
                        Toast.makeText(this, "No se encontró el ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void buscar() {
        listar(db.buscarProductos(etBuscar.getText().toString().trim()));
    }

    private void listar(Cursor c) {
        listaProductos.clear();
        while (c.moveToNext()) {
            listaProductos.add(new Producto(
                    c.getInt(0),      // id_producto
                    c.getString(1),   // codigo
                    c.getString(2),   // nombre
                    c.getString(3),   // categoria (nombre vía JOIN)
                    c.getString(7),   // descripcion
                    c.getDouble(4),   // precio
                    c.getInt(5),      // stock
                    c.getString(8)    // imagen (nombre del recurso drawable)
            ));
        }
        c.close();
        adapter.actualizarLista(listaProductos);
    }
}