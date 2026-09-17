
# TecnoMarketApp — Sistema de Gestión Comercial Android

Repositorio oficial de TecnoMarketApp, solución móvil nativa para Android orientada a la administración comercial, control de inventario, facturación y reportes analíticos en tiempo real.

Proyecto desarrollado durante la sesión práctica de laboratorio del 01 de septiembre de 2026, enfocado en arquitectura móvil por capas, persistencia de datos local con SQLite y patrón visual Fintech.

---

## Objetivo de la Práctica
Diseñar e implementar una aplicación Android modular y escalable que cumpla con:
* Operaciones transaccionales CRUD completas para inventario y ventas.
* Filtros de búsqueda predictiva en tiempo real implementando TextWatcher.
* Integridad referencial mediante consultas avanzadas (INNER JOIN) en SQLite (DBHelper).
* Pantallas de exhibición de detalle técnico (Showcase) para productos y comprobantes.
* Consultas analíticas de rendimiento comercial y alertas de existencias.

---

## Stack Tecnológico y Arquitectura

* Lenguaje: Java 17 / Android SDK
* Persistencia: SQLite Nativo (SQLiteOpenHelper)
* Interfaz de Usuario: Material Design Components (MaterialCardView, MaterialButton, TextInputLayout)
* Estilo Visual: Fintech Header (Fondos curvos seguros ante notch y barra de estado)
* Control de Versiones: Git y GitHub

---

## Módulos Implementados

### 1. Panel de Control (MainActivity)
* Métricas superiores: Indicadores en vivo de productos registrados, clientes activos y total de comprobantes emitidos, actualizados de forma automática en el ciclo de vida onResume().
* Control de acceso: Bloqueo dinámico de acceso al módulo de usuarios para cuentas sin permisos de administrador (ADMIN).
* Acción rápida: Acceso directo a la emisión de comprobantes.

### 2. Módulo de Productos y Categorías
* Registro, actualización y baja de artículos vinculados a categorías dinámicas mediante ItemSpinner.
* Ficha técnica (DetalleProductoActivity): Visualización detallada del artículo, stock disponible, precio unitario y descripción de fábrica sin dependencias externas.

### 3. Módulo de Ventas y Facturación
* Formulario transaccional con selección de cliente asistida por ItemSpinner.
* Cálculo automático: Detección de subtotal, IGV (18%) y total neto facturado.
* Historial transaccional: Listado dinámico con RecyclerView y VentaAdapter que permite filtrar por cliente o ID de comprobante.
* Comprobante Showcase (DetalleVentaActivity): Vista tipo ticket electrónico con desglose de unidades, importe total y confirmación de salida de almacén.

### 4. Módulo de Clientes y Usuarios
* Mantenimiento de datos de contacto y documentos de identidad de clientes.
* Administración de accesos y asignación de roles operativos (ADMIN, VENDEDOR, CAJERO) con ocultamiento seguro de credenciales.

### 5. Analítica y Reportes
* Top 5 Productos Más Vendidos (TopProductosActivity): Ranking de artículos con mayor volumen de rotación ordenados por unidades y monto recaudado.
* Top 5 Mayores Ventas (TopVentasActivity): Ranking de comprobantes de mayor facturación comercial.
* Alertas de Stock Crítico (ReporteStockActivity): Detección preventiva de artículos próximos a agotarse en almacén.

---

## Estructura del Código Fuente

```text
com.example.tecnomarketapp/
│
├── data/
│   └── DBHelper.java                    # Sentencias DDL, DML y consultas analíticas SQLite
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
│   ├── MainActivity.java                # Tablero principal de métricas y navegación
│   ├── LoginActivity.java               # Autenticación y validación de roles
│   ├── ProductosActivity.java           # Gestión y listado de productos
│   ├── DetalleProductoActivity.java     # Pantalla showcase del producto
│   ├── CategoriasActivity.java          # Mantenimiento de líneas de producto
│   ├── ClientesActivity.java            # Directorio de clientes
│   ├── UsuariosActivity.java            # Administración de accesos
│   ├── VentasActivity.java              # Emisión y gestión de comprobantes
│   ├── DetalleVentaActivity.java        # Ficha digital del comprobante emitido
│   ├── TopProductosActivity.java        # Reporte de productos más vendidos
│   ├── TopVentasActivity.java           # Reporte de mayores facturaciones
│   └── ReporteStockActivity.java        # Reporte de existencias mínimas
│
└── res/layout/
    ├── activity_main.xml                # Dashboard principal con métricas
    ├── activity_ventas.xml              # Formulario y listado de ventas
    ├── activity_detalle_venta.xml       # Showcase de desglose de venta
    ├── activity_top_ventas.xml          # Diseño de analítica de ventas
    ├── item_venta.xml                   # Tarjeta personalizada de la venta
    └── ...                              # Demás layouts del proyecto
'@
