package com.example.tecnomarketapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TecnoMarket.db";
    private static final int DB_VERSION = 2; // Incrementado para actualizar automáticamente

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "usuario TEXT UNIQUE NOT NULL," +
                "correo TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "rol TEXT NOT NULL," +
                "estado INTEGER DEFAULT 1," +
                "fecha_registro TEXT NOT NULL)");

        db.execSQL("CREATE TABLE categorias (" +
                "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT UNIQUE NOT NULL," +
                "descripcion TEXT," +
                "estado INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE productos (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "codigo TEXT UNIQUE NOT NULL," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "id_categoria INTEGER NOT NULL," +
                "precio REAL NOT NULL," +
                "stock INTEGER NOT NULL," +
                "imagen TEXT," +
                "estado INTEGER DEFAULT 1," +
                "FOREIGN KEY(id_categoria) REFERENCES categorias(id_categoria))");

        db.execSQL("CREATE TABLE clientes (" +
                "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni TEXT UNIQUE NOT NULL," +
                "nombres TEXT NOT NULL," +
                "apellidos TEXT NOT NULL," +
                "telefono TEXT," +
                "correo TEXT," +
                "direccion TEXT," +
                "estado INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE ventas (" +
                "id_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_cliente INTEGER NOT NULL," +
                "id_usuario INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "subtotal REAL NOT NULL," +
                "igv REAL NOT NULL," +
                "total REAL NOT NULL," +
                "FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente)," +
                "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario))");

        db.execSQL("CREATE TABLE detalle_venta (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_venta INTEGER NOT NULL," +
                "id_producto INTEGER NOT NULL," +
                "cantidad INTEGER NOT NULL," +
                "precio_unitario REAL NOT NULL," +
                "importe REAL NOT NULL," +
                "FOREIGN KEY(id_venta) REFERENCES ventas(id_venta)," +
                "FOREIGN KEY(id_producto) REFERENCES productos(id_producto))");

        // Carga de datos iniciales
        insertarDatosSemilla(db);
    }

    private void insertarDatosSemilla(SQLiteDatabase db) {
        // 1. USUARIOS (4 registros)
        db.execSQL("INSERT INTO usuarios (nombre, usuario, correo, password, rol, estado, fecha_registro) VALUES " +
                "('Administrador General', 'admin', 'admin@tecnomarket.com', 'admin123', 'ADMIN', 1, '2026-08-01')," +
                "('Jazmin Chamorro', 'jchamorro', 'jchamorro@tecnomarket.com', 'jaz123', 'ADMIN', 1, '2026-08-10')," +
                "('Roberto Flores', 'rflores', 'rflores@tecnomarket.com', 'cajero123', 'CAJERO', 1, '2026-08-15')," +
                "('Elena Ramos', 'eramos', 'eramos@tecnomarket.com', 'ventas123', 'VENDEDOR', 1, '2026-08-20');");

        // 2. CATEGORÍAS (4 registros)
        db.execSQL("INSERT INTO categorias (nombre, descripcion, estado) VALUES " +
                "('Muebles de Oficina', 'Escritorios ergonómicos y archivadores', 1)," +
                "('Tecnología', 'Laptops, computadoras y componentes', 1)," +
                "('Accesorios', 'Periféricos, cables y soportes', 1)," +
                "('Audio', 'Auriculares profesionales y parlantes', 1);");

        // 3. PRODUCTOS (4 registros con imágenes correspondientes en drawable)
        db.execSQL("INSERT INTO productos (codigo, nombre, descripcion, id_categoria, precio, stock, imagen, estado) VALUES " +
                "('MUB-001', 'Escritorio Ejecutivo', 'Melamina 18mm con pasacables', 1, 489.00, 15, 'mueble', 1)," +
                "('TEC-002', 'Laptop Pro 15', 'Intel i7 16GB RAM 512GB SSD', 2, 3499.00, 3, 'tecnomarket_logo', 1)," +
                "('ACC-003', 'Soporte Monitor Doble', 'Brazo hidráulico articulado', 3, 149.50, 20, 'tecnomarket_logo', 1)," +
                "('AUD-004', 'Audífonos Bluetooth', 'Cancelación activa de ruido 30h', 4, 259.00, 2, 'tecnomarket_logo', 1);");

        // 4. CLIENTES (4 registros)
        db.execSQL("INSERT INTO clientes (dni, nombres, apellidos, telefono, correo, direccion, estado) VALUES " +
                "('71234567', 'Carlos Alberto', 'Gomez Ruiz', '987654321', 'carlos.gomez@gmail.com', 'Av. Javier Prado Este 1250', 1)," +
                "('72345678', 'Mariana Lucia', 'Torres Vega', '976543210', 'mariana.torres@hotmail.com', 'Jr. Las Palmeras 430', 1)," +
                "('73456789', 'David Esteban', 'Mendoza Quispe', '965432109', 'david.mendoza@yahoo.com', 'Calle Los Jazmines 215', 1)," +
                "('74567890', 'Valeria Sofia', 'Chavez Morales', '954321098', 'valeria.chavez@gmail.com', 'Av. Arequipa 3200', 1);");

        // 5. VENTAS (4 transacciones)
        db.execSQL("INSERT INTO ventas (id_cliente, id_usuario, fecha, subtotal, igv, total) VALUES " +
                "(1, 1, '2026-08-25', 978.00, 176.04, 1154.04)," +
                "(2, 2, '2026-08-28', 3499.00, 629.82, 4128.82)," +
                "(3, 3, '2026-09-01', 299.00, 53.82, 352.82)," +
                "(4, 4, '2026-09-02', 259.00, 46.62, 305.62);");

        // 6. DETALLE DE VENTAS (Mapeo de unidades para probar Top Productos)
        db.execSQL("INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, importe) VALUES " +
                "(1, 1, 2, 489.00, 978.00)," +
                "(2, 2, 1, 3499.00, 3499.00)," +
                "(3, 3, 2, 149.50, 299.00)," +
                "(4, 4, 1, 259.00, 259.00);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS detalle_venta");
        db.execSQL("DROP TABLE IF EXISTS ventas");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    // LOGIN
    public Cursor validarLogin(String usuario, String password) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM usuarios WHERE usuario=? AND password=? AND estado=1",
                new String[]{usuario, password});
    }

    // USUARIOS
    public long insertarUsuario(String nombre, String usuario, String correo,
                                String password, String rol, int estado, String fecha) {
        ContentValues v = new ContentValues();
        v.put("nombre", nombre);
        v.put("usuario", usuario);
        v.put("correo", correo);
        v.put("password", password);
        v.put("rol", rol);
        v.put("estado", estado);
        v.put("fecha_registro", fecha);
        return getWritableDatabase().insert("usuarios", null, v);
    }

    public Cursor mostrarUsuarios() {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM usuarios ORDER BY id_usuario DESC", null);
    }

    public int actualizarUsuario(int id, String nombre, String usuario,
                                 String correo, String password, String rol, int estado) {
        ContentValues v = new ContentValues();
        v.put("nombre", nombre);
        v.put("usuario", usuario);
        v.put("correo", correo);
        v.put("password", password);
        v.put("rol", rol);
        v.put("estado", estado);
        return getWritableDatabase().update(
                "usuarios", v, "id_usuario=?", new String[]{String.valueOf(id)});
    }

    public int eliminarUsuario(int id) {
        return getWritableDatabase().delete(
                "usuarios", "id_usuario=?", new String[]{String.valueOf(id)});
    }

    // CATEGORIAS
    public long insertarCategoria(String nombre, String descripcion, int estado) {
        ContentValues v = new ContentValues();
        v.put("nombre", nombre);
        v.put("descripcion", descripcion);
        v.put("estado", estado);
        return getWritableDatabase().insert("categorias", null, v);
    }

    public Cursor mostrarCategorias() {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM categorias ORDER BY nombre", null);
    }

    public int actualizarCategoria(int id, String nombre, String descripcion, int estado) {
        ContentValues v = new ContentValues();
        v.put("nombre", nombre);
        v.put("descripcion", descripcion);
        v.put("estado", estado);
        return getWritableDatabase().update(
                "categorias", v, "id_categoria=?", new String[]{String.valueOf(id)});
    }

    public int eliminarCategoria(int id) {
        return getWritableDatabase().delete(
                "categorias", "id_categoria=?", new String[]{String.valueOf(id)});
    }

    // PRODUCTOS
    public long insertarProducto(String codigo, String nombre, String descripcion,
                                 int idCategoria, double precio, int stock,
                                 String imagen, int estado) {
        ContentValues v = new ContentValues();
        v.put("codigo", codigo);
        v.put("nombre", nombre);
        v.put("descripcion", descripcion);
        v.put("id_categoria", idCategoria);
        v.put("precio", precio);
        v.put("stock", stock);
        v.put("imagen", imagen);
        v.put("estado", estado);
        return getWritableDatabase().insert("productos", null, v);
    }

    public Cursor mostrarProductos() {
        return getReadableDatabase().rawQuery(
                "SELECT p.id_producto,p.codigo,p.nombre,c.nombre AS categoria," +
                        "p.precio,p.stock,p.id_categoria,p.descripcion,p.imagen,p.estado " +
                        "FROM productos p INNER JOIN categorias c " +
                        "ON p.id_categoria=c.id_categoria ORDER BY p.nombre", null);
    }

    public int actualizarProducto(int id, String codigo, String nombre, String descripcion,
                                  int idCategoria, double precio, int stock,
                                  String imagen, int estado) {
        ContentValues v = new ContentValues();
        v.put("codigo", codigo);
        v.put("nombre", nombre);
        v.put("descripcion", descripcion);
        v.put("id_categoria", idCategoria);
        v.put("precio", precio);
        v.put("stock", stock);
        v.put("imagen", imagen);
        v.put("estado", estado);
        return getWritableDatabase().update(
                "productos", v, "id_producto=?", new String[]{String.valueOf(id)});
    }

    public int eliminarProducto(int id) {
        return getWritableDatabase().delete(
                "productos", "id_producto=?", new String[]{String.valueOf(id)});
    }

    public Cursor buscarProductos(String texto) {
        return getReadableDatabase().rawQuery(
                "SELECT p.id_producto,p.codigo,p.nombre,c.nombre AS categoria," +
                        "p.precio,p.stock FROM productos p INNER JOIN categorias c " +
                        "ON p.id_categoria=c.id_categoria " +
                        "WHERE p.nombre LIKE ? OR p.codigo LIKE ? ORDER BY p.nombre",
                new String[]{"%" + texto + "%", "%" + texto + "%"});
    }

    // CLIENTES
    public long insertarCliente(String dni, String nombres, String apellidos,
                                String telefono, String correo, String direccion, int estado) {
        ContentValues v = new ContentValues();
        v.put("dni", dni);
        v.put("nombres", nombres);
        v.put("apellidos", apellidos);
        v.put("telefono", telefono);
        v.put("correo", correo);
        v.put("direccion", direccion);
        v.put("estado", estado);
        return getWritableDatabase().insert("clientes", null, v);
    }

    public Cursor mostrarClientes() {
        return getReadableDatabase().rawQuery(
                "SELECT * FROM clientes ORDER BY apellidos,nombres", null);
    }

    public int actualizarCliente(int id, String dni, String nombres, String apellidos,
                                 String telefono, String correo, String direccion, int estado) {
        ContentValues v = new ContentValues();
        v.put("dni", dni);
        v.put("nombres", nombres);
        v.put("apellidos", apellidos);
        v.put("telefono", telefono);
        v.put("correo", correo);
        v.put("direccion", direccion);
        v.put("estado", estado);
        return getWritableDatabase().update(
                "clientes", v, "id_cliente=?", new String[]{String.valueOf(id)});
    }

    public int eliminarCliente(int id) {
        return getWritableDatabase().delete(
                "clientes", "id_cliente=?", new String[]{String.valueOf(id)});
    }

    // VENTAS
    public long insertarVenta(int idCliente, int idUsuario, String fecha,
                              double subtotal, double igv, double total) {
        ContentValues v = new ContentValues();
        v.put("id_cliente", idCliente);
        v.put("id_usuario", idUsuario);
        v.put("fecha", fecha);
        v.put("subtotal", subtotal);
        v.put("igv", igv);
        v.put("total", total);
        return getWritableDatabase().insert("ventas", null, v);
    }

    public Cursor mostrarVentas() {
        return getReadableDatabase().rawQuery(
                "SELECT v.id_venta,v.fecha," +
                        "c.nombres || ' ' || c.apellidos AS cliente," +
                        "u.nombre AS vendedor,v.subtotal,v.igv,v.total " +
                        "FROM ventas v " +
                        "INNER JOIN clientes c ON v.id_cliente=c.id_cliente " +
                        "INNER JOIN usuarios u ON v.id_usuario=u.id_usuario " +
                        "ORDER BY v.id_venta DESC", null);
    }

    public int actualizarVenta(int id, int idCliente, double subtotal,
                               double igv, double total) {
        ContentValues v = new ContentValues();
        v.put("id_cliente", idCliente);
        v.put("subtotal", subtotal);
        v.put("igv", igv);
        v.put("total", total);
        return getWritableDatabase().update(
                "ventas", v, "id_venta=?", new String[]{String.valueOf(id)});
    }

    public int eliminarVenta(int id) {
        getWritableDatabase().delete(
                "detalle_venta", "id_venta=?", new String[]{String.valueOf(id)});
        return getWritableDatabase().delete(
                "ventas", "id_venta=?", new String[]{String.valueOf(id)});
    }

    // DETALLE
    public long insertarDetalle(int idVenta, int idProducto, int cantidad,
                                double precioUnitario, double importe) {
        ContentValues v = new ContentValues();
        v.put("id_venta", idVenta);
        v.put("id_producto", idProducto);
        v.put("cantidad", cantidad);
        v.put("precio_unitario", precioUnitario);
        v.put("importe", importe);
        return getWritableDatabase().insert("detalle_venta", null, v);
    }

    public Cursor mostrarDetalle(int idVenta) {
        return getReadableDatabase().rawQuery(
                "SELECT d.id_detalle,p.nombre,d.cantidad," +
                        "d.precio_unitario,d.importe " +
                        "FROM detalle_venta d INNER JOIN productos p " +
                        "ON d.id_producto=p.id_producto " +
                        "WHERE d.id_venta=? ORDER BY d.id_detalle",
                new String[]{String.valueOf(idVenta)});
    }

    public int descontarStock(int idProducto, int cantidad) {
        ContentValues v = new ContentValues();
        v.put("stock", "stock - " + cantidad);
        return getWritableDatabase().update(
                "productos", v, "id_producto=? AND stock>=?",
                new String[]{String.valueOf(idProducto), String.valueOf(cantidad)});
    }

    // CONSULTAS Y REPORTES
    public Cursor productosStockBajo(int limite) {
        return getReadableDatabase().rawQuery(
                "SELECT codigo,nombre,stock FROM productos " +
                        "WHERE stock<=? AND estado=1 ORDER BY stock ASC",
                new String[]{String.valueOf(limite)});
    }

    public Cursor totalVendido() {
        return getReadableDatabase().rawQuery(
                "SELECT COALESCE(SUM(total),0) AS total_vendido FROM ventas", null);
    }

    public Cursor topProducto() {
        return getReadableDatabase().rawQuery(
                "SELECT p.nombre,SUM(d.cantidad) AS unidades " +
                        "FROM detalle_venta d INNER JOIN productos p " +
                        "ON d.id_producto=p.id_producto " +
                        "GROUP BY p.id_producto,p.nombre " +
                        "ORDER BY unidades DESC LIMIT 1", null);
    }

    public Cursor obtenerCategoriasSpinner() {
        return getReadableDatabase().rawQuery(
                "SELECT id_categoria, nombre FROM categorias WHERE estado = 1 ORDER BY nombre ASC", null);
    }

    public Cursor obtenerClientesSpinner() {
        return getReadableDatabase().rawQuery(
                "SELECT id_cliente, (nombres || ' ' || apellidos) AS cliente FROM clientes WHERE estado = 1 ORDER BY apellidos ASC", null);
    }

    public int contarProductos() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM productos WHERE estado = 1", null);
        int total = 0;
        if (c.moveToFirst()) total = c.getInt(0);
        c.close();
        return total;
    }

    public int contarClientes() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM clientes WHERE estado = 1", null);
        int total = 0;
        if (c.moveToFirst()) total = c.getInt(0);
        c.close();
        return total;
    }

    public int contarVentas() {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM ventas", null);
        int total = 0;
        if (c.moveToFirst()) total = c.getInt(0);
        c.close();
        return total;
    }

    public Cursor top5ProductosVendidos() {
        return getReadableDatabase().rawQuery(
                "SELECT p.nombre, SUM(d.cantidad) AS unidades, " +
                        "COALESCE(SUM(d.importe), 0) AS total_recaudado " +
                        "FROM detalle_venta d " +
                        "INNER JOIN productos p ON d.id_producto = p.id_producto " +
                        "GROUP BY p.id_producto, p.nombre " +
                        "ORDER BY unidades DESC LIMIT 5", null);
    }

    public Cursor filtrarVentasPorFecha(String fechaInicio, String fechaFin) {
        return getReadableDatabase().rawQuery(
                "SELECT v.id_venta, v.fecha, " +
                        "c.nombres || ' ' || c.apellidos AS cliente, " +
                        "u.nombre AS vendedor, v.subtotal, v.igv, v.total " +
                        "FROM ventas v " +
                        "INNER JOIN clientes c ON v.id_cliente = c.id_cliente " +
                        "INNER JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                        "WHERE date(v.fecha) BETWEEN date(?) AND date(?) " +
                        "ORDER BY v.fecha DESC",
                new String[]{fechaInicio, fechaFin});
    }

    public int obtenerStockProducto(int idProducto) {
        int stock = 0;
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT stock FROM productos WHERE id_producto = ?",
                new String[]{String.valueOf(idProducto)});
        if (c.moveToFirst()) {
            stock = c.getInt(0);
        }
        c.close();
        return stock;
    }

    public boolean insertarDetalleConTransaccion(int idVenta, int idProducto, int cantidad,
                                                 double precioUnitario, double importe) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            int stockActual = obtenerStockProducto(idProducto);
            if (stockActual < cantidad) {
                return false;
            }

            ContentValues v = new ContentValues();
            v.put("id_venta", idVenta);
            v.put("id_producto", idProducto);
            v.put("cantidad", cantidad);
            v.put("precio_unitario", precioUnitario);
            v.put("importe", importe);

            long resultadoDetalle = database.insert("detalle_venta", null, v);
            if (resultadoDetalle == -1) {
                return false;
            }

            database.execSQL(
                    "UPDATE productos SET stock = stock - ? WHERE id_producto = ?",
                    new Object[]{cantidad, idProducto});

            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            database.endTransaction();
        }
    }

    public Cursor verificarStockProducto(int idProducto) {
        return getReadableDatabase().rawQuery(
                "SELECT nombre, stock FROM productos WHERE id_producto = ? AND estado = 1",
                new String[]{String.valueOf(idProducto)});
    }
}