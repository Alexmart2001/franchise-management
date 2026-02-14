# Proyecto Base Implementando Clean Architecture

## Descripción del Proyecto

El proyecto está diseñado para manejar una lista de franquicias, sucursales y productos ofertados mediante una API reactiva empleando **Spring Boot WebFlux**. La implementación se desarrolla utilizando la **Arquitectura Hexagonal (Clean Architecture)**, lo que permite aislar los detalles técnicos y centrarse en las reglas del negocio.

Las entidades principales son:
1. **Franquicia**: Compuesta de un nombre y lista de sucursales.
2. **Sucursal**: Compuesta de un nombre y lista de productos ofertados.
3. **Producto**: Compuesto de un nombre y un stock.

---

## Arquitectura del Proyecto

El proyecto fue desarrollado siguiendo la Clean Architecture, dividiendo en capas independientes como se describe a continuación:

### 1. **Domain**
Encapsula la lógica y reglas del negocio mediante modelos y entidades. Aquí se encuentran las definiciones principales relacionadas con los objetos **Franquicias**, **Sucursales**, y **Productos**.

### 2. **Usecases**
Es quien implementa los casos de uso del sistema. Define la lógica de los flujos y orquesta las interacciones entre entidades.

### 3. **Infrastructure**
#### Helpers
Incluye clases genéricas que facilitan la implementación de patrones como **Repository** y **Unit of Work**, útiles para interactuar con sistemas de persistencia externa.

#### Driven Adapters
Implementan conexiones externas como bases de datos (PostgreSQL en este caso), servicios REST, o cualquier interacción con fuentes de datos externas.

#### Entry Points
Define los puntos de entrada al sistema, expose controladores REST para interactuar con los flujos del negocio.

### 4. **Application**
Encargada de ensamblar todos los módulos, resolver dependencias y ejecutar la aplicación con el método `main`. Implementa un `@ComponentScan` que asegura la disponibilidad automática de los beans definidos en las capas internas.

---

## Requisitos Técnicos

### **Tecnologías Requeridas**
- **Framework**: Spring Boot 3 y WebFlux.
- **Base de Datos**: PostgreSQL (utilizando Docker).
- **Clean Architecture**: Estructura de módulos creada desde el plugin oficial Scaffold.
- **Automatización de tareas**: Gradle.
- **Lenguaje de programación**: Java 17 o superior.
- **APIs**: Estilo RESTful.
- **Pruebas Unitarias**: JUnit asegurando una cobertura superior al 60%.
- **Logs**: Implementados con SLF4J/Log4j.

---

## Instalación y Ejecución del Proyecto

### Requisitos Previos
- **Java**: JDK 17 o superior instalado y configurado en el PATH.
- **Gradle**: Versión 9.2.1 o superior instalado (o utiliza el wrapper del proyecto).
- **Docker**: Para configurar la base de datos PostgreSQL localmente.

### Pasos para implementar el Proyecto

#### **1. Generación del Proyecto Base**
Ejecuta los siguientes comandos en tu terminal. 

```bash
# Crear carpeta de proyecto
mkdir franchise-management
cd franchise-management

# Crear el archivo build.gradle con el plugin Clean Architecture
(
echo plugins {
echo     id 'co.com.bancolombia.cleanArchitecture' version '4.0.5'
echo }
) > build.gradle

# Generar el wrapper para Gradle
gradle wrapper

# Generar el proyecto base
./gradlew ca --name=franchise-management

# Crear entry point de tipo Webflux
./gradlew gep --type webflux

# Proyecto Base: Franquicia Management

## Configuración de la Base de Datos

### Crear las Tablas
Ejecuta el siguiente script para crear las tablas en PostgreSQL:

```sql
CREATE TABLE franchise (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE branch (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id INTEGER NOT NULL,
    FOREIGN KEY (franchise_id) REFERENCES franchise(id) ON DELETE CASCADE
);

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL CHECK (stock >= 0),
    branch_id INTEGER NOT NULL,
    FOREIGN KEY (branch_id) REFERENCES branch(id) ON DELETE CASCADE
);


# Proyecto Base: Gestión de Franquicias

## Configuración de la Conexión a la Base de Datos
Modifica el archivo `application.yaml` ubicado en la ruta `applications/app-service/src/main/resources/` para incluir los datos de conexión:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/franchise_db
    username: admin
    password: nequiTest123


# Proyecto: Gestión de Franquicias

## Ejecución y Verificación

Ejecuta el proyecto localmente utilizando el siguiente comando en la terminal:

```bash
./gradlew bootRun


# Endpoints Disponibles

### 1. **Crear Franquicia**
**POST** `/api/franchises`

#### Cuerpo de la petición:
```json
{
  "name": "Franquicia A"
}

# Endpoints Disponibles

### 2. **Crear Sucursal para una Franquicia**
**POST** `/api/franchises/{franchiseId}/branches`

#### Cuerpo de la petición:
```json
{
  "name": "Sucursal A"
}


### 3. **Agregar Producto a una Sucursal**
**POST** `/api/branches/{branchId}/products`

#### Cuerpo de la petición:
```json
{
  "name": "Producto X",
  "stock": 150
}


### 4. **Modificar Stock de un Producto**
**PUT** `/api/branches/{branchId}/products/{productId}`

#### Cuerpo de la petición:
```json
{
  "stock": 200
}

### 5. **Consultar el Producto con Mayor Stock**
**GET** `/api/franchises/{franchiseId}/top-products`

#### Respuesta esperada:
```json
[
  {
    "productName": "Producto X",
    "stock": "200",
    "branchName": "Sucursal A"
  },
  {
    "productName": "Producto Y",
    "stock": "250",
    "branchName": "Sucursal B"
  }
]

## Extras (Opcionales)

- **Actualizar el nombre de Franquicia**, **Sucursal**, o **Producto** utilizando métodos **PUT**.

---

## Decisiones de Diseño

### 1. **Entorno Reactivo con Spring WebFlux**
Todas las librerías utilizadas son compatibles con el entorno reactivo, asegurando un manejo eficiente de las señales:
- `onNext`
- `onError`
- `onComplete`.

---

### 2. **Operadores Reactivos**
Los flujos de datos se manejan mediante operadores reactivos como:
- `map`
- `flatMap`
- `switchIfEmpty`
- `zip`.

Estos operadores garantizan:
- **Encadenabilidad óptima** de las respuestas.
- **Mayor eficiencia** en la lógica y procesamiento de los flujos de negocio.


### 3. **Base de Datos PostgreSQL**
PostgreSQL fue seleccionada por sus características:
- **Robusteza** y capacidad para manejar datos relacionales.
- Excelente compatibilidad con entornos **containerizados** utilizando Docker.
- La base de datos **se encuentra montada en la nube**, lo que asegura escalabilidad, accesibilidad y alta disponibilidad para el proyecto.

---

### 4. **Pruebas Unitarias**
Pruebas realizadas mediante **JUnit 5**, obteniendo:
- **Cobertura mínima asegurada de 60%**.
- **Cobertura lograda del 72%**, superando el objetivo mínimo y validando la lógica del negocio de manera aceptable.

### 5. **Estado del Proyecto**
El proyecto está **listo para ser ejecutado localmente** una vez se cumplan los requerimientos previos. Levanta la aplicación utilizando el siguiente comando:

```bash
./gradlew bootRun