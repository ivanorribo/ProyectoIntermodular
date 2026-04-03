# Hidragri
Proyecto intermodular de DAM
## Funcionalidades prototipo (mvp)
- Sistema de autenticación de usuarios con credenciales encriptadas.
- Gestión del territorio (CRUD). Administración con altas, bajas y actualizaciones.
- Bitácora de cultivos. Trazabilidad de plantaciones vinculadas a parcelas.
- Ingesta de consumos hídricos. Módulo de telemetría que permite registro manual o importación masiva mediante CSV.
- Reportes. Generación de informes en formato PDF.

## Requisitos y ejecución
- Disponer de Java JDK 21 o superior.
- Contar con un servidor MySQL. Para el prototipo se ha usado XAMPP y las conexiones de la BD están hechas de momento para local host.
- Importar la estructura de la base de datos. Se encuentra en la carpeta "extras" como BD.sql. Para el prototipo se usó mediante phpmyadmin.
- Para pruebas y crear usuarios en el prototipo. Hay que usar la clase de PasswordUtil ya que genera el dash que hay que añadir al usuario creado en la base de datos.
- La clase Main.java es la que lanza el programa mediante su interfaz gráfica.
- Para testing del importe masivo mediante csv. Se añade un documento csv en la carpeta "extras".