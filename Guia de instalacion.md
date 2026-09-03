# Guía de Instalación y Ejecución — TecnoMarketApp

Este documento describe de manera detallada los requisitos técnicos previos, el procedimiento de clonación, la configuración del entorno en **Android Studio** y el despliegue del proyecto **TecnoMarketApp** tanto en emuladores virtuales como en dispositivos físicos.

---

## 1. Requisitos Previos del Sistema

Antes de iniciar con el despliegue, verifique que su estación de trabajo cuente con los siguientes componentes configurados:

* **Sistema Operativo:** Windows 10/11 (64-bit), macOS o Linux.
* **Java Development Kit (JDK):** Versión 17 o superior.
* **Android Studio:** Versión Hedgehog, Ladybug o superior con el **Android SDK** actualizado.
* **Control de Versiones:** **Git** versión 2.40 o superior instalado y reconocido en las variables de entorno del sistema (`PATH`).
* **Nivel de API Android:** Mínimo **API 26** (Android 8.0 Oreo), con destino recomendado en **API 34** (Android 14).
* **Almacenamiento:** Mínimo 4 GB de espacio disponible en disco para compilación, dependencias de Gradle y caché local.

---

## 2. Clonación del Repositorio Remoto

Abra una ventana de terminal (**Git Bash**, **PowerShell** o la consola integrada de su sistema) y ejecute el comando para clonar el repositorio:

```bash
git clone https://github.com/jazmin-boop/TecnoMarketApp.git
```

Acceda a la carpeta raíz del proyecto:

```bash
cd TecnoMarketApp
```

---

## 3. Apertura y Sincronización en Android Studio

1. Inicie **Android Studio**.
2. Seleccione la opción **Open** y elija la carpeta raíz `TecnoMarketApp`.
3. Espere a que el proceso de **Gradle Sync** finalice la descarga de dependencias nativas (`Material Components`, `RecyclerView`, `AppCompat`).
4. Si el IDE solicita la instalación de componentes faltantes del SDK, acepte las descargas sugeridas.

---

## 4. Credenciales de Acceso y Configuración Inicial

La aplicación cuenta con una base de datos local **SQLite** (`tecnomarket.db`) que se autoinicializa en el primer arranque mediante la clase `DBHelper.java`. No requiere servidores externos ni configuraciones de red adicionales.

### Cuentas preconfiguradas para pruebas:

* **Usuario Administrador:**
  * **Usuario:** `admin`
  * **Contraseña:** `admin123`
  * **Rol:** `ADMIN` (Acceso total: gestión de usuarios, productos, categorías, clientes, facturación y reportes analíticos).

* **Usuario Vendedor (Opcional para pruebas de rol):**
  * **Usuario:** `vendedor`
  * **Contraseña:** `123456`
  * **Rol:** `VENDEDOR` (Restringido: el módulo de cuentas de usuario se deshabilita automáticamente por validación de seguridad en `MainActivity`).

---

## 5. Parámetros y Configuraciones Importantes

* **Cálculo Financiero de Facturación:** El sistema aplica automáticamente una tasa fija de **IGV del 18%** sobre las ventas emitidas (`subtotal * 0.18 = igv`, `subtotal + igv = total`).
* **Manejo de Imágenes sin Dependencias Externas:** El proyecto no utiliza librerías de terceros como Glide o Picasso. Las imágenes de productos se procesan directamente desde los recursos locales (`res/drawable/`) mediante el identificador dinámico de Android SDK.
* **Control de Permisos de Interfaz:** La pantalla principal (`MainActivity`) ejecuta una verificación insensible a mayúsculas/minúsculas (`ADMIN` / `ADMINISTRADOR`) para bloquear accesos no autorizados al mantenimiento de usuarios.
* **Persistencia Relacional:** Las ventas y detalles de transacción utilizan restricciones de clave foránea vinculadas a `productos` y `clientes`. La eliminación de un cliente o producto con historial impedirá la rotura de consistencia en el almacén central.

---

## 6. Ejecución del Proyecto

### En Emulador Virtual (AVD):
1. Abra **Tools > Device Manager** en Android Studio.
2. Inicie un dispositivo virtual con imagen **API 26** o superior.
3. Presione el botón **Run** (`Shift + F10`).

### En Dispositivo Móvil Físico:
1. En su teléfono Android, active la **Depuración por USB** desde las **Opciones de desarrollador**.
2. Conecte el dispositivo mediante cable USB y autorice la conexión en pantalla.
3. Seleccione su teléfono en la barra superior de Android Studio y presione **Run**.
