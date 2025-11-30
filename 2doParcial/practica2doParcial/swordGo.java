/*
DSI - Segundo Parcial
SwordGo
Enunciado: https://docs.google.com/document/d/1ySg2AAbg6wHGIqfPlwbKgEphnpbi9HVbgOJrMo3qI9s/edit?tab=t.0
Solución oficial: https://docs.google.com/document/d/10uEstaNVua88KKaUKln1tMxsArbKjfj_SKlmO6ck5mI/edit?tab=t.0
*/

// A - Persistencia Relacional

/*
- las coordenadas son objetos que solo guardan valores, asi que los embebo dentro del personaje y la ubicacion
- como  el TipoPersonaje es stateless, lo transformo en enum al que el personaje tiene una referencia
- transformo item en clase abstracta para poder persistir
- uso SINGLE_TABLE como estrategia de mapeo de herencia para los items porque imagino que se harán muchas consultas polimórficas a los mismos constantemente, cómo que el personaje consulte todos los items de su inventario, y además muchas columnas son compartidas entre varias de las subclases, lo que haría que la cantidad de columnas en NULL sea menor. Aunque en un momento se aclara que habrá consultas no polimórficas, para lo que sería mejor usar una estrategia JOINED por la cantidad de columnas compartidas, considero que probablemente la frecuencia de esas consultas no polimórficas sea menor que las polimórficas.
*/

@MappedSuperclass
abstract class PersistenceEntity{
    @Id
    @GeneratedValue
    private long id;
}

@Entity
class Personaje extends PersistanceEntity{
    private String nombre;
    private int monedas;

    @Enumerated(EnumType.STRING)
    private TipoPersonaje tipo;

    @Embedded
    private Coordenadas coordenadas;

    @ManyToMany
    @JoinTable(
        name="ubicacionesVisitadas",
        joinColumns = @JoinColumn(name="personaje_id"),
        inverseJoinColumns = @JoinColumn(name="ubicacion_id")
    )
    @OrderColumn(name="ultimas visitadas")
    private List<Ubicacion> ubicacionesVisitadas;

    @OneToMany(mappedBy="duenio")
    private List<Item> items;

    @OneToMany(mappedBy = "accesorio_en_uso_por")
    private List<Accesorio> accesoriosEnUso;
    
    @OneToMany(mappedBy = "arma_en_uso_por")
    private List<Arma> armasEnUso;
}

@Entity
class Ubicacion extends PersistanceEntity{
    private String nombre;

    @Embedded
    private Coordenadas coordenadas;

    @OneToMany(mappedBy = "ubicacion")
    private List<Item> itemsPresentes;
}

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
abstract class Item extends PersistanceEntity{

    private String nombre;
    private int costo

    @ManyToOne
    @JoinColumn(name="duenio_id")
    private Personaje duenio;

    @ManyToOne
    @JoinColumn(name="ubicacion_id")
    private Ubicacion ubicacion;

    @Embedded
    private Coordenadas coordenadas;

    public int costo(){...}
    public String nombre(){...}
}

@Entity
@DiscriminatorValue("bolsaDeMonedas")
class BolsaDeMonedas extends Item{
    private int monedas;

    @Override
    public String nombre(){
        return "Bolsa De Monedas"
    }

    @Override
    public int costo(){
        return monedas;
    }
}

@Entity
@DiscriminatorValue("regalo")
class Regalo extends Item{
    private String fraseMotivacional;

    @Override
    public String nombre(){
        return "Regalo"
    }

    @Override
    public int costo(){
        return 0
    }
}

@Entity
@DiscriminatorValue("accesorio")
class Accesorio extends Item{
    private int costo;
    private String nombre;

    @ManyToOne
    @JoinColumn(name="accesorio_en_uso_por")
    private Personaje accesorio_en_uso_por;
}

@Entity
@DiscriminatorValue("arma")
class Arma extends Item{
    private int costo;
    private String nombre;
    private int ataque;
    private int defensa;

    @ManyToOne
    @JoinColumn(name="arma_en_uso_por")
    private Personaje arma_en_uso_por;
}

@Embeddable
class Coordenadas{
    private int x;
    private int y;
    private int nivel;
}

public enum TipoPersonaje {
    GUERRERO("Guerrero"),
    COMERCIANTE("Comerciante"),
    HECHICERO("Hechicero")
}

//  B - Interfaz REST

/*
supongo que manejamos sesiones, por lo que el id del usuario no será necesario de modelar en las rutas
Entiendo que HTML sólo permite hacer GET y POST, por lo que para usar otros verbos http tendría que usar js

- Crear nuevos ítems, especificando su nombre, precio y características específicas
POST /items
body: "tipo=itemTipo&nombre=itemNombre&precio=itemPrecio&caracteristicas=itemCaracteristicas"

- Editar un ítem, ajustando su precios o sus características
PATCH /items/:id
con un body similar al anterior, pero sólo con los atributos a actualizar

Deshabilitar un ítem, impidiendo su comercio y distribución en el juego, pero sin provocar cambios en los personajes y ubicaciones que los tuvieran
PATCH items/:id
usaría la misma ruta que la anterior pero en el body enviaría un "activo=false", siendo activo un atributo booleano que debería tener cada item que se verificaría a la hora de comercializarse y distribuirse

Exponer un API REST para buscar y consultar estadísticas públicas de cada personaje en juego, para que se puedan desarrollar otros negocios en torno al juego.
GET /estadisticas -> para estadisticas generales del juego, cómo cuentas registradas totales, número de jugadores activos, número de jugadores máximos simultaneos, etc

GET /personajes/:id/estadisticas -> para acceder a las stats de un jugador en particular

Luego, para obtener las pantallas bocetadas:
GET /items/new -> para obtener el formulario de creación

GET /items/:id/edicion -> para obtener el formulario de edición

GET /items -> para mostrar todos los items

componentes html reutilizables:
- Navbar
- Cards para los items
- Botones
*/

// C - Arquitectura

/*
1.a) Deberíamos analizar la escalabilidad.
1.b) Para cumplir con el requerimiento sin cambiar la arquitectura actual, podríamos escalar verticalmente la capacidad del servidor, pero esto tiene un límite.
1.c) Agregaría más servidores, para distribuir la carga entre ellos con un balanceador de carga.

2) Si es posible resolver la situación mencionada con la arquitectura actual, podrían implementarse websockets, posibilitando que el servidor envíe información al cliente cuando este está conectado.

3) Podría agregarse una caché, dónde se guardaría la ubicación del personaje en tiempo real, y en la base de datos se sincroniza cada cierto tiempo.
*/