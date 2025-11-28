@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
class Contenido{
    @Id
    @GeneratedValue
    private long id;

    private int reproducciones;
    private String imagenDeTapa;
    
    @ManyToOne
    @JoinColumn(name="duenio_id")
    private Usuario duenio;

    @Enumerated(EnumType.STRING)
    private Clasificacion clasificacion;

    @Embedded
    private Estadistica estadistica;
}

@Entity
@DiscriminatorValue("PODCAST")
class Podcast extends Contenido{
    private Date fechaInicio;
    private Date fechaFin;
}

@Entity
@DiscriminatorValue("CANCION")
class Cancion extends Contenido{
    private float duracion;
    private Date fechaSubida;
}

@Entity
class PlayList{
    @Id
    @GeneratedValue
    private Long id; 

    @Enumerated(EnumType.STRING)
    private Visibilidad visibilidad;
    
    @ManyToOne
    @JoinColumn(name="duenio_id")
    private Usuario duenio;

    @OneToMany(MappedBy="playlist")
    private List<PlayListXcontenido> contenidos;

    @ManyToMany
    @JoinTable(
        name="suscripciones",
        joinColumns=@JoinColumn(name="playList_id")
        inverseJoinColumns = @JoinColumn(name="usuario_id")
    )
    private List<Usuario> suscriptores; 
}

@Entity
class PlayListXcontenido {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="playlist_id")
    private PlayList playList;
    
    @ManyToOne
    @JoinColumn(name="contenido_id")
    private Contenido contenido; 

    private int posicion
}   

@Embaddable
class Estadistica{
    private int likes
    private int dislikes
}

@Entity
class Usuario{
    @Id
    @GeneratedValue
    private long id;
}

public enum Clasificacion {
    MENORES{
        @Override 
        public boolean validarAcceso(Usuario usuario){return ...};
    },
    ADOLESCENTES{
        @Override 
        public boolean validarAcceso(Usuario usuario){return ...};
    },
    ADULTOS{
        @Override 
        public boolean validarAcceso(Usuario usuario){return ...};
    }
}

public enum Visibilidad {
    PUBLICA,
    PRIVADA,
    NO_LISTADA
}

///////////////////////////////////////////////////////////////////

//Asumiendo que no hay un token
GET /usuarios/:id/
//Usaría js ya que html solamente permite realizar post o gets en los forms. 
PATCH /usuarios/:id/
PUT /usuarios/:id/

//Si tuviesemos un token -> mejor
GET /perfil
//Usaría js ya que html solamente permite realizar post o gets en los forms. 
PUT /perfil
PATCH /perfil

//Esto carga la vista que esta dibujada
GET /contenidos/:id

//Se pueden buscar otras contenidos (los resultados se visualizarán en otra pantalla)
GET /contenidos?song=songTitle

//Se puede iniciar la reproducción (recordar que cada vez que se reproduce una canción 
//es necesario actualizar el contador de reproducciones)
POST /contenidos/:id/reproduccion -> se lee cómo: crea una reproduccion, sumando uno a las reproducciones

//Se puede dar me gusta o deshacer el me gusta dado (cuidado: deshacer el me gusta 
// no es lo mismo que dar no me gusta, este requerimiento por ahora no 
// estará contemplado en la UI)
PATCH /contenidos/:id/like ->  Medio bien, es un post y un DELETE

//correcto:
POST /contenidos/:id/likes
DELETE /contenidos/:id/likes

// Editor de listas de reproducción: desde allí podemos ver los contenidos de una lista de producción. Una vez allí: 	
GET /playslists/:id/

// Se puede eliminar contenidos de la lista (esto tiene efecto al instante, sin tener que hacer guardar)
DELETE playLists/:idPlaylist/contenidos/:idCancion/

// Se puede cambiar el nombre de la lista
// Se puede guardar los cambios (por ahora el único cambio que se guardará es el cambio del nombre)
// entrar a una lista de reproduccion, carga la vista dibujada.
PATCH /playList/:id/

///////////////////////////////////////////////////////////////////

