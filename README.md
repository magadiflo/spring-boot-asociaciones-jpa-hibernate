# Asociaciones de entidades usando JPA/Hibernate (Spring Data JPA): Unidireccionales y Bidireccionales

## Asociaciones Unidireccionales

### One To One

- La anotación @OneToOne en un tipo de relación unidireccional podría
  estar en cualquiera de las dos entidades relacionadas (Port o Service).
  Decidir en qué entidad irá la relación, dependerá del análisis que hagamos y
  cómo quisiéramos manejarlo.
- En nuestro caso, decidimos que la **entidad Port será dueña de la relación**,
  por lo tanto, la anotación **@OneToOne** la colocaremos en dicha entidad.
- Agregamos además la anotación **@JoinColumn(name = "service_id", unique = true)**
  en nuestra entidad dueña de la relación. Por defecto, el nombre de la Foreign Key
  generada por hibernate es igual al **nombre-del-atributo-definido_id (service_id)**.
  Si quisiéramos cambiar dicho nombre, usamos la anotación **@JoinColumn** y en el
  atributo name le definimos el nuevo nombre. En nuestro caso, estamos colocando
  el mismo nombre que por defecto nos generaría hibernate **service_id**, no habría problemas
  simplemente estamos siendo explícitos. Además, un punto importante, es que nos aseguraremos
  de que la relación sea de **one-to-one** añadiéndole una restricción de **unique=true**.
- En la anotación @OneToOne(cascade = {CascadeType.ALL}), agregamos el **CascadeType.ALL**,
  con esto podremos persistir, eliminar, etc.. un Port y su Service en cascada. Por ejemplo,
  si se quiere guardar un registro para Port, aprovechar ese guardado para poder guardar
  también su correspondiente Service.
- Nuestras dos entidades quedarían de la siguiente manera:

````
@Entity
@Table(name = "ports")
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private String type;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "service_id", unique = true)
    private Service service;
    
    # Setters, Getters, toString()...
````

````
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    
    # Setters, Getters, toString()...
````

- Las tablas generadas en la Base de Datos serían:  
  ![OneToOne unidireccional](/assets/unidireccional_one-to-one.png)
- En este tipo de relación unidireccional, la Foreign Key estará en la tabla
  cuya clase de entidad tiene la anotación @OneToOne (dueña de la relación),
  esto significa entonces, que nuestra tabla Ports de la BD tendrá la
  Foreign Key (service_id) de Services.

#### Listando todos los Ports

Como resultado observamos que nos devuelve la lista de los Ports junto a su
service relacionado.

```
[GET] http://localhost:8080/unidireccional/v1/one-to-one/ports
```

```
[
    {
        "id": 1,
        "number": 3306,
        "type": "tcp",
        "service": {
            "id": 1,
            "name": "MySQL",
            "path": "C:\\laragon\\bin\\mysql\\mysql-5.7.33-winx64\\bin"
        }
    }
]
```

#### Guardando Port

Solo queremos guardar datos del Port, sin datos para su service. Como resultado
se obtiene null en el atributo **service**

````
[POST] http://localhost:8080/unidireccional/v1/one-to-one/ports
````

**Request**

````
{
    "number": 8080,
    "type": "tcp"
}
````

**Response**

````
{
    "id": 7,
    "number": 8080,
    "type": "tcp",
    "service": null
}
````

#### Guardando Port y su Service

Guardamos datos del Port y su Service enviados en un mismo objeto JSON.

````
[POST] http://localhost:8080/unidireccional/v1/one-to-one/ports
````

**Request**

````
{
    "number": 3924,
    "type": "tcp",
    "service": {
        "name": "DiagTrack",
        "path": "C:\\WINDOWS\\System32\\svchost.exe"
    }
}
````

**Response**

````
{
    "id": 8,
    "number": 3924,
    "type": "tcp",
    "service": {
        "id": 2,
        "name": "DiagTrack",
        "path": "C:\\WINDOWS\\System32\\svchost.exe"
    }
}
````

#### Editando solo port (no tiene service)

Enviamos solo datos del port para poder editarlo:

````
[PUT] http://localhost:8080/unidireccional/v1/one-to-one/ports/6
````

**Request**

````
{
    "number": 5536,
    "type": "UDP"
}
````

**Response**

````
{
    "id": 6,
    "number": 5536,
    "type": "UDP",
    "service": null
}
````

#### Editando solo port (sí tiene service)

Enviamos solo datos del port para poder editarlo:

````
[PUT] http://localhost:8080/unidireccional/v1/one-to-one/ports/8
````

**Request**

````
{
    "number": 3939,
    "type": "TCP"
}
````

**Response**

````
{
    "id": 8,
    "number": 3939,
    "type": "TCP",
    "service": {
        "id": 2,
        "name": "DiagTrack",
        "path": "C:\\WINDOWS\\System32\\svchost.exe"
    }
}
````

#### Editando port y service

Enviamos datos tanto del port y service para editarlos

````
[PUT] http://localhost:8080/unidireccional/v1/one-to-one/ports/8
````

**Request**

````
{
    "number": 3939,
    "type": "TCP",
    "service": {
        "id": 2,
        "name": "Service Diag-Track",
        "path": "C:\\WINDOWS\\System32\\svchost-track.exe"
    }
}
````

**Response**

````
{
    "id": 8,
    "number": 3939,
    "type": "TCP",
    "service": {
        "id": 2,
        "name": "Service Diag-Track",
        "path": "C:\\WINDOWS\\System32\\svchost-track.exe"
    }
}
````

#### Editando Port creándole un Service

A un Port que no tiene un registro de service lo editaremos para
registrarle un service

````
[PUT] http://localhost:8080/unidireccional/v1/one-to-one/ports/6
````

**Request**

````
{
    "number": 5536,
    "type": "tcp",
    "service": {
        "name": "Servicio Windows Defender",
        "path": "C:\\WINDOWS\\System32\\windows-defender.exe"
    }
}
````

**Response**

````
{
    "id": 6,
    "number": 5536,
    "type": "tcp",
    "service": {
        "id": 3,
        "name": "Servicio Windows Defender",
        "path": "C:\\WINDOWS\\System32\\windows-defender.exe"
    }
}
````

#### Eliminando Port

Cuando eliminamos un port, eliminará al registro cuyo id pasado por parámetro
corresponda al Port en la BD y si este registro tiene relacionado un service,
lo eliminará en cascada. De allí la razón por la que colocamos el CascadeType.ALL

````
[DELETE] http://localhost:8080/unidireccional/v1/one-to-one/ports/8
````
