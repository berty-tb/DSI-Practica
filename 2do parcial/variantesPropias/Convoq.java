@MappedSuperclass
class PersistanceEntity{
    @Id
    @GeneratedValue
    private Long id;
}

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
class Convocatoria extends PersistanceEntity{
    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="convocante_id")
    private Usuario convocante;
    
    @Embedded
    private Modalidad modalidad; 
}

@Entity
@DiscriminatorValue("Publica")
class ConvocatoriaPublica extends Convocatoria{}

@Entity
@DiscriminatorValue("Privada")
class ConvocatoriaPrivada extends Convocatoria{}

@Entity
@DiscriminatorValue("Hibrida")
class ConvocatoriaHibrida extends Convocatoria{}

@Entity
class Usuario extends PersistanceEntity {
    private String nombreCompleta; 
    private LocalDateTime fechaDeNacimiento;
    private Boolean esInstitucion;

    @Enumerated(EnumType.STRING)
    private Pais nacionalidad; 
}

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
class EventoAuditoria extends PersistanceEntity{}

@Entity
class EventoUsuario extends EventoAuditoria {
    private LocalDateTime ocurridoEn;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento;

    @Enumerated(EnumType.STRING)
    private Ejecutor ejecutor; 
    private String urlEjecutor;
}

@Entity
class EventoPostulacion extends EventoAuditoria {
    private LocalDateTime actualizadoEn; 
    private String rolActualizador; 
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="actualizador_id")
    private Usuario actualizador;

    @ManyToOne
    @JoinColumn(name="postulacion_id")
    private Postulacion postulacion;
}

@Entity
class Postulacion extends PersistanceEntity {
    private String titulo;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name="convocatoria_id")
    private Convocatoria Convocatoria;

    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @ManyToOne
    @JoinColumn(name="postulante_id")
    private Usuario postulante;
}

@Entity
class Comentario extends PersistanceEntity {
    private String texto; 
    private LocalDateTime fecha; 
    @ManyToOne
    @JoinColumn(name="emisor_id")
    private Usuario emisor;
    @ManyToOne
    @JoinColumn(name="postulacion_id")
    private Postulacion postulacion; 
}
 
@Embeddable
class Modalidad {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin; 
}

enum Pais{
    ARGENTINA,
    BRASIL, 
    BOLIVIA
}

enum EstadoPostulacion{
    NUEVO,
    ENVIADA,
    EN_REVISION,
    APROBADA, 
    RECHAZADA
}

enum TipoEvento {
    LOGIN, 
    LOGOUT, 
    SIGNUP,
    DELETE
}

enum Ejecutor{
    SISTEMA_EXTERNO, 
    SISTEMA_INTERNO,
    HUMANO
}

//Cargar formulario de convocatoria
GET /convocatorias/new

//Crear convocatoria
POST /convocatorias

//Mostrar 10 convocatorias publicas
//Por query param...
GET /convocatorias
//Ejemplo
GET /convocatorias?tipo=publica&size=10&sort=fechaInicio&order=desc

//Alta de postulacion
POST /convocatorias/:id/postulacion

//Si el usuario no esta logeado
GET /login

//Imagino que el id del usuario esta en la sesion y con eso recupero las postulaciones
//de sus convocatorias
GET /postulaciones

//para aceptar o rechazar
PUT /postulaciones/:id?accion=rechazar
PUT /postulaciones/:id?accion=aceptar

//Para ver detalle
GET /postulaciones/:id