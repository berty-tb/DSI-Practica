/*
DSI - Segundo Parcial
SwordGo
Enunciado
*/
@Entity
public class Personaje {
    @Id
    @GeneratedValue
    private Long id;

    private int nombre;
    private int monedas;
    private int x;
    private int y;
    private int nivel;
    @Enumerated(EnumType.STRING)
    private TipoPersonaje tipo;
    @ManyToMany
    @JoinTable(
        name="ubicacionesVisitadas",
        joinColumns = @JoinColumn(name="personaje_id"),
        inverseJoinColumns= @JoinColumn(name="ubicacion_id")
    )
    private List<Ubicacion> ubicacionesVisitadas;
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Item {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Portador_id")
    private SwordGo portador;
    private String name;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public abstract coste();
}

@Entity
class Arma extends Item {
    private int costo;
    private int defense;
    private int ataque;

    @ManyToOne
    @JoinColumn(name = "Usuario_id")
    private SwordGo usuario;

    @Override
    public int coste() {
        return costo;
    }
}

@Entity
class Accesorio extends Item {
    private int costo;
    @ManyToOne
    @JoinColumn(name = "Usuario_id")
    private SwordGo usuario;

    @Override
    public int coste() {
        return costo;
    }
}

@Entity
@DiscriminatorValue("REGALO")
class Regalo extends Item {

    public Regalo() {
        super("Regalo");
    }

    @Override
    public int coste() {
        return 0;
    }
}

@Entity
@DiscriminatorValue("BOLSAMONEDAS")
class BolsaDeMonedas extends Item {
    private int monedas;

    public BolsaDeMonedas() {
        super("Bolsa de moneda");
    }

    @Override
    public int coste() {
        return monedas;
    }
}

public enum TipoPersonaje {
    GUERRERO("Guerrero"),
    COMERCIANTE("Comerciante"),
    HECHICERO("Hechicero");
}

/*
Realmente ubicacion se podria modelar de dos maneras:
i) Si necesito que la navegabilidad sea bidireccional, lo hago asi.
ii) Si puede ser unidireccional (solo necesito saber las ubicaciones de
un personaje) podria modelarlo asi:
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
}
*/
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "ubicacionesVisitadas")
    private List<SwordGo> personajes;
}


<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8" />
  <title>{{title}}</title>
</head>
<body>
  <h1>{{title}}</h1>

  {{!-- Condicional simple --}}
  {{#if user}}
    <p>Hola, {{user.name}} 👋</p>
  {{else}}
    <p>Hola, invitado. <a href="/login">Iniciá sesión</a></p>
  {{/if}}

  <h2>Items</h2>
  {{#if items.length}}
    <ul>
      {{#each items}}
        <li>
          <strong>{{this.nombre}}</strong> — ${{this.precio}}
          {{#if this.inactivo}} <em>(inactivo)</em> {{/if}}
        </li>
      {{/each}}
    </ul>
  {{else}}
    <p>No hay items para mostrar.</p>
  {{/if}}
</body>
</html>
