@MappedSuperclass
class PersistanceEntity{
    @Id
    @GeneratedValue
    private Long id;
}

@Entity
class Item extends PersistanceEntity{
    @ManyToOne
    @JoinColumn(name="emisor_id")
    private Usuarie emisor;

    @ManyToOne
    @JoinColumn(name="id_canal")
    private Canal canal;
}

@Entity
class Usuarie extends PersistanceEntity {
    @ManyToMany
    @JoinTable(
        name="usuarieXcanal",
        joinColumns=@JoinColumn(name="usuarie_id"),
        inverseJoinColumns=@JoinColumn(name="canal_id")
    )
    private List<Canal> canales;
}

@Entity
class Canal extends PersistanceEntity {
}

@Entity
class Difusion extends Canal{
    @ManyToOne
    @JoinColumn(name="owner")
    private Usuarie owner;
}

@Entity
class Dialogo extends Canal{
    @ManyToOne
    @JoinColumn(name="participante1")    
    private Usuarie participante1;
    
    @ManyToOne
    @JoinColumn(name="participante2")   
    private Usuarie participante2;
}

@Entity
class Mensaje extends PersistanceEntity{
    @ManyToOne
    @JoinColumn(name="canal")
    private Canal canal;

    @ManyToOne
    @JoinColumn(name="emisor")
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name="visibilidad")
    private Visibilidad visibilidad;
}

@Entity
@Inheritance(InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Tipo")
class Visibilidad{}

@Entity
@DiscriminatorValue(name="Instantanea")
class Instantanea extends Visibilidad{}

@Entity
@DiscriminatorValue(name="Estandar")
class Estandar extends Visibilidad{}

@Entity
@DiscriminatorValue(name="Instantanea")
class Temporal extends Visibilidad{
    @Enumerated(EnumType.STRING)
    private Duracion duracion;
}

@Entity
class Adjunto extends PersistanceEntity{
    @ManyToOne
    @JoinColumn(name="mensaje_id")
    private Mensaje mensaje; 
}