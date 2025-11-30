@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipoEvento", type=DiscriminatorType.STRING)
abstract class Evento{
    @Id
    @GeneratedValue
    private Long id; 

    @ManyToOne
    @JoinColumn(name="user_id")
    private Usuario usuario;
    
    private DateTime inicio;
    private DateTime fin;

    @ManyToOne
    @JoinColumn(name="city_id")
    private Ciudad ciudad;
}

@Entity
@DiscriminatorValue(name="Unico")
class EventoUnico extends Evento{
    ...
}

@Entity
@DiscriminatorValue(name = "Recurrente")
class EventoRecurrente extends Evento{
    @Enumerated(EnumType.STRING)
    private Recurrencia recurrencia;

    ...
}

@Entity
class Ciudad{
    @Id
    @GeneratedValue
    private Long id;

    private String key;
}

@Entity
class Usuario{
    @Id
    @GeneratedValue
    private Long id;
}

@Entity
class Prenda{
    @Id
    @GeneratedValue
    private Long id;
    
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name="owner")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name="ColorXPrenda",
        joinColumns=@JoinColumn(name="prenda_id"),
        inverseJoinColumns=@JoinColumn(name="color_id")
    )
    private List<Color> colores;
}

@Entity
class Color{
    @Id
    @GeneratedValue
    private Long id;
    
    private Sting descripcion;
    private String codigoHexa;
}

public enum Recurrencia{
    SEMANAL{
        @Override
        public DateTime proximaOcurrencia(Evento evento) { ... }
    },
    MENSUAL{
        @Override
        public DateTime proximaOcurrencia(Evento evento) { ... }
    }
}

public enum Categoria{
    ARRIBA{
        @Override
        public boolean cubreTemperatura(float temperatura){...}
    }, 
    ABAJO{
        @Override
        public boolean cubreTemperatura(float temperatura){...}
    },
    ENTERO{
        @Override
        public boolean cubreTemperatura(float temperatura){...}
    }, 
    CALZADO{
        @Override
        public boolean cubreTemperatura(float temperatura){...}
    }, 
    ACCESORIO{
        @Override
        public boolean cubreTemperatura(float temperatura){...}
    }
}


//Asumiendo que le guardarrope es unico
GET /guardarrope/
//Asumiendo que pueden haber muchos guardarropes
GET /guardarrope/:id -> Bulga no lo contempló

//Cargar formulario de creacion
GET /prendes/new

//pare crear une prende:
POST /prendes
//Asumiende que haye variese guardarrope
POST /guardarrope/:id/prendes

//Elementos HTML
Formularie de creacione
Botone pare nueve prende
Link a detalle de le prende -> GET /prendes/:id

Layout tendria la navbar y un footer.