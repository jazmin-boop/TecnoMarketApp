Set-Content -Path README.md -Value @'
# TecnoMarketApp — Sistema de Gestion Comercial Android

Repositorio oficial de TecnoMarketApp, solucion movil nativa para Android orientada a la administracion comercial, control de inventario, facturacion y reportes analiticos en tiempo real.

Proyecto desarrollado durante la sesion practica de laboratorio del 01 de septiembre de 2026, enfocado en arquitectura movil por capas, persistencia de datos local con SQLite y patron visual Fintech.

---

## Objetivo de la Practica
Disenar e implementar una aplicacion Android modular y escalable que cumpla con:
* Operaciones transaccionales CRUD completas para inventario y ventas.
* Filtros de busqueda predictiva en tiempo real implementando TextWatcher.
* Integridad referencial mediante consultas avanzadas (INNER JOIN) en SQLite (DBHelper).
* Pantallas de exhibicion de detalle tecnico (Showcase) para productos y comprobantes.
* Consultas analiticas de rendimiento comercial y alertas de existencias.

---

## Stack Tecnologico y Arquitectura

* Lenguaje: Java 17 / Android SDK
* Persistencia: SQLite Nativo (SQLiteOpenHelper)
* Interfaz de Usuario: Material Design Components (MaterialCardView, MaterialButton, TextInputLayout)
* Estilo Visual: Fintech Header (Fondos curvos seguros ante notch y barra de estado)
* Control de Versiones: Git y GitHub

---

## Modulos Implementados

### 1. Panel de Control (MainActivity)
* Metricas superiores: Indicadores en vivo de productos registrados, clientes activos y total de comprobantes emitidos, actualizados de forma automatica en el ciclo de vida onResume().
* Control de acceso: Bloqueo dinamico de acceso al modulo de usuarios para cuentas sin permisos de administrador (ADMIN).
* Accion rapida: Acceso directo a la emision de comprobantes.

### 2. Modulo de Productos y Categorias
* Registro, actualizacion y baja de articulos vinculados a categorias dinamicas mediante ItemSpinner.
* Ficha tecnica (DetalleProductoActivity): Visualizacion detallada del articulo, stock disponible, precio unitario y descripcion de fabrica sin dependencias externas.

### 3. Modulo de Ventas y Facturacion
* Formulario transaccional con seleccion de cliente asistida por ItemSpinner.
* Calculo automatico: Deteccion de subtotal, IGV (18%) y total neto facturado.
* Historial transaccional: Listado dinamico con RecyclerView y VentaAdapter que permite filtrar por cliente o ID de comprobante.
* Comprobante Showcase (DetalleVentaActivity): Vista tipo ticket electronico con desglose de unidades, importe total y confirmacion de salida de almacen.

### 4. Modulo de Clientes y Usuarios
* Mantenimiento de datos de contacto y documentos de identidad de clientes.
* Administracion de accesos y asignacion de roles operativos (ADMIN, VENDEDOR, CAJERO) con ocultamiento seguro de credenciales.

### 5. Analitica y Reportes
* Top 5 Productos Mas Vendidos (TopProductosActivity): Ranking de articulos con mayor volumen de rotacion ordenados por unidades y monto recaudado.
* Top 5 Mayores Ventas (TopVentasActivity): Ranking de comprobantes de mayor facturacion comercial.
* Alertas de Stock Critico (ReporteStockActivity): Deteccion preventiva de articulos proximos a agotarse en almacen.

---

## Estructura del Codigo Fuente

```text
com.example.tecnomarketapp/
│
├── data/
│   └── DBHelper.java                    # Sentencias DDL, DML y consultas analiticas SQLite
│
├── models/
│   ├── Producto.java                    # Clase entidad Producto
│   ├── Venta.java                       # Clase entidad Venta
│   └── ItemSpinner.java                 # Clase utilitaria clave-valor para desplegables
│
├── adapters/
│   ├── ProductoAdapter.java             # Adaptador de listado de inventario
│   └── VentaAdapter.java                # Adaptador para el historial de comprobantes
│
├── ui/
│   ├── MainActivity.java                # Tablero principal de metricas y navegacion
│   ├── LoginActivity.java               # Autenticacion y validacion de roles
│   ├── ProductosActivity.java           # Gestion y listado de productos
│   ├── DetalleProductoActivity.java     # Pantalla showcase del producto
│   ├── CategoriasActivity.java          # Mantenimiento de lineas de producto
│   ├── ClientesActivity.java            # Directorio de clientes
│   ├── UsuariosActivity.java            # Administracion de accesos
│   ├── VentasActivity.java              # Emision y gestion de comprobantes
│   ├── DetalleVentaActivity.java        # Ficha digital del comprobante emitido
│   ├── TopProductosActivity.java        # Reporte de productos mas vendidos
│   ├── TopVentasActivity.java           # Reporte de mayores facturaciones
│   └── ReporteStockActivity.java        # Reporte de existencias minimas
│
└── res/layout/
    ├── activity_main.xml                # Dashboard principal con metricas
    ├── activity_ventas.xml              # Formulario y listado de ventas
    ├── activity_detalle_venta.xml       # Showcase de desglose de venta
    ├── activity_top_ventas.xml          # Diseno de analitica de ventas
    ├── item_venta.xml                   # Tarjeta personalizada de la venta
    └── ...                              # Demas layouts del proyecto
