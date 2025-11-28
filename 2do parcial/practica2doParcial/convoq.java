/*
DSI - Segundo Parcial
Convoq
Enunciado: https://docs.google.com/document/d/1Qmr7Rh2MaDAndHVEzX5OsO1gsyjQY48KNtey2zKRR7Q/edit?tab=t.0
*/

// A - Persistencia Relacional

/*
- Uso la estrategia SINGLE_TABLE para persistir la convocatoria, pues sus subclases simplemente tienen comportamiento diferente, no atributos propios.
- Uso la estrategia SINGLE_TABLE para persistir la modalidad (que transformo de interfaz a clase abstracta para poder persistirla), pues una sola de sus subclases tiene atributos y probablemente quiera acceder a las modalidades de forma polimórfica.
- Embebo las modalidades dentro de la convocatoria, terminan siendo un value object.
- Uso la estrategia TABLE_PER_CLASS para persistir los eventos, interfaz que transformo en clase abstracta para poder persistirla, porque solo cada 3 meses se harán consultas sobre TODOS los eventos y no comparten ningún atributo.
*/

@MappedSuperclass
abstract class PersistenceEntity{
    @Id
    @GeneratedValue
    private long id;
}

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_convocatoria")
class Convocatoria extends PersistenceEntity{
    private String nombre;
    private descripcion;

    @ManyToOne
    @JoinColumn(name="convocante_id")
    private Usuario convocante;

    @Embedded
    private ModalidadConvocatoria modalidad;
}

@Embeddable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_modalidad")
class ModalidadConvocatoria{}

@DiscriminatorValue("ventanilla_permanente")
class VentanillaPermanente extends ModalidadConvocatoria{}

@DiscriminatorValue("con_vencimiento")
class ConVencimiento extends ModalidadConvocatoria{
    private Date fechaInicio;
    private Date fechaFin;
}

@Entity
class Usuario extends PersistenceEntity{
    private String nombreCompleto;
    private Date fechaDeNacimiento;
    private boolean esInstitucion;

    @Enumerated(EnumType.STRING)
    private Nacionalidad Nacionalidad;
}

@Entity
class Postulacion extends PersistenceEntity{
    private String titulo;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estado;

    @ManyToOne
    @JoinColumn(name="postulante_id")
    private Usuario postulante;

    @ManyToOne
    @JoinColumn(name="convocatoria_id")
    private Convocatoria convocatoria;
}

@Entity
class Comentario extends PersistenceEntity{
    private String texto;
    private Date fecha;
    
    @ManyToOne
    @JoinColumn(name="comentarista_id")
    private Usuario comentarista;

    @ManyToOne
    @JoinColumn(name="postulacion_id")
    private Postulacion postulacion;
}

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
abstract class EventoAuditoria{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
}

@Entity
class EventoUsuario extends EventoAuditoria{
    private Date ocurridoEn;

    @Enumerated(EnumType.STRING)
    private TipoEventoUsuario tipoEvento;

    @Enumerated(EnumType.STRING)
    private Ejecutor ejecutor;

    private String urlEjecutor;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
}

@Entity
class EventoPostulacion extends EventoAuditoria{
    private Date actualizadoEn;

    @ManyToOne
    @JoinColumn(name="actualizador_id")
    private Usuario actualizadoPor;

    private String descripcion;
    private String rolActualizador;

    @ManyToOne
    @JoinColumn(name="postulacion_id")
    private Postulacion postulacion;
}

// B - Interfaz REST

/*
RUTAS:

soy consciente de que html solo permite hacer GET y POST, por lo que para usar otro verbo http usaria js

Alta de convocatoria: una persona administrativa de la parte convocante debe poder crear una nueva convocatoria, especificando su nombre, descripción y período (opcional) durante el cual estará abierta. Además, desde esta pantalla se deberá indicar si la convocatoria es pública, privada o híbrida. 

GET /convocatorias/new -> formulario de creacion
POST /convocatorias -> crear una convocatoria

Listado de convocatorias públicas: una postulante debe poder entrar sin necesidad de loguearse y ver todas las últimas 10 convocatorias públicas abiertas. Desde este listado debe ser posible navegar a la pantalla de alta de postulaciones (pasando por el login si la postulante no inició sesión aún)

GET /convocatorias -> listado de convocatorias publicas
GET /conovcatorias/:id/postulaciones/new -> formulario de alta de postulacion

Panel de control de postulaciones: una persona administrativa de la convocante debe poder ver todas las postulaciones enviadas a cualquiera de las convocatorias de la institución convocante. Desde este panel de control, debe poder ver los datos básicos de la postulación, aceptar o rechazar cada una de las postulaciones, o navegar hacia otra pantalla con el detalle de la misma. Tené en cuenta que este listado puede ser muy extenso. 

GET /postulaciones -> listado de postulaciones de cualquier convocatoria de una organizacion (la que tenga la sesion iniciada)
PATCH /postulaciones/:id -> para aceptar o rechazar
con un body del estilo: aceptada=true
GET /postulaciones/:id
*/

// C - Arquitectura

/*
La plataforma actualmente está al límite de su capacidad de atención de pedidos HTTP. Ya se ha mejorado varias veces la cantidad de memoria y núcleos del servidor, pero sigue siendo insuficiente. Además, se desea maximizar la disponibilidad de la plataforma.
1 - Cómo escalar verticalmente no es una opción, escalaría horizontalmente agregando mas servidores que corran la aplicacion, distribuyendo la carga entre ellos con un balanceador.

Por regulaciones de ciertos países, Convoq debe analizar automáticamente los contenidos de las postulaciones y reportar ciertas alarmas de índole impositiva. Si bien éste servicio de auditoría y notificación ya se encuentra desarrollado por otro equipo e integrado a través de un API REST, demora demasiado en responder. Tené en cuenta que no es una opción viable modificar su código actualmente y que la respuesta de este servicio no es relevante para el resto del sistema.
2 - Implementaria una cola de mensajes que cada servidor llenaría para que no se bloqueen esperando la respuesta de la API, luego un worker se encargaría de vaciar esa cola y hacer los pedidos a la API, actualizando el estado de las postulaciones en la base de datos con su respuesta.

Convoq se usa de forma global y algunos clientes nos han reportado que sus imágenes y estilos tardan en cargar.
3 - Usaría un CDN que sirva los archivos estáticos.
*/