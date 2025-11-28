@MappedSuperclass
class PersistanceEntity{
    @Id
    @GeneratedValue
    private Long id;
}

@Entity
class Casa extends PersistanceEntity {
    private String nombre;
    private int patrimonio;
    private int anioFundacion;

    @ManyToOne
    @JoinColumn(name="origen")
    private Lugar origen;

    @ManyToOne
    @JoinColumn(name="vasallaDe_id")
    private Casa vasallaDe;

    @OneToMany
    @JoinColumn(name="casa_id")
    private List<FuerzaMilitar> fuerzaMilitares;
}

@Entity
class Region extends PersistanceEntity {
    private String nombre;

    @ManyToOne
    @JoinColumn(name="casa_id")
    private Casa casaPrincipal;

    @ManyToMany
    @JoinTables(
        name="regionXlugares",
        joinColumns=@JoinColumn(name="region_id"),
        inverseJoinColumns=@JoinColumn(name="lugar_id")
    )
    private List<Lugar> lugares; 
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Lugar extends PersistanceEntity {
    private String nombre;
    private int anioFundacion;
    private int poblacion;
}

@Entity
class Castillo extends Lugar{
    private int cantidadTorres;
    private int cantidadMurallas;
}

@Entity
class Ciudad extends Lugar {
    private int cantidadDeComercios;
    private int cantidadDeSantuarios;
    private double tasaDeMortalidad;
}

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
abstract class FuerzaMilitar extends PersistanceEntity {
    public abstract void atacarA(Lugar lugar);
}

@Entity
@DiscriminatorValue("naval")
class Naval extends FuerzaMilitar {
    private int cantidadBarcos;
}

@Entity
@DiscriminatorValue("terrestre")
class Terrestre extends FuerzaMilitar {
    private int cantidadSoldados;
}

@Entity
@DiscriminatorValue("aerea")
class Aerea extends FuerzaMilitar {
    private int cantidadDragones;
}