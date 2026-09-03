package com.example.tecnomarketapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsuario, etPassword;
    Button btnIngresar;
    TextView tvMensaje;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        tvMensaje = findViewById(R.id.tvMensaje);

        dbHelper = new DBHelper(this);

        btnIngresar.setOnClickListener(v -> {

            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                tvMensaje.setText("Complete usuario y contraseña");
                return;
            }

            Cursor cursor = dbHelper.validarLogin(usuario, password);

            if (cursor.moveToFirst()) {

                int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String rol = cursor.getString(cursor.getColumnIndexOrThrow("rol"));

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("nombreUsuario", nombre);
                intent.putExtra("rol", rol);
                startActivity(intent);
                finish();

            } else {
                tvMensaje.setText("Usuario o contraseña incorrectos");
                Toast.makeText(this, "Acceso denegado", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });
    }
}
