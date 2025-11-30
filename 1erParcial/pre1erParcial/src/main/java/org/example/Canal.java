package org.example;

import java.util.ArrayList;
import java.util.List;

public class Canal {
    private final Usuario propietario;
    private final List<Transmision> transmisionesHistoricas = new ArrayList<>();
    private Transmision transmisionActual = null;
    private final List<Integer> muestrasDeApoyo = new ArrayList<>();
    private final List<Usuario> sucriptores = new ArrayList<>();
    private final RepoCanales repoCanales = RepoCanales.getInstance();

    public Canal(Usuario propietario) {
        this.propietario = propietario;
        repoCanales.agregarCanal(this);
    }

    public List<Transmision> listarTransmisionesHistoricas() {
        return transmisionesHistoricas;
    }

    public void transmitir(String titulo, List<String> categorias) {
        if (transmisionActual != null) {
            throw new IllegalStateException("Ya se está transmitiendo");
        }
        transmisionActual = new Transmision(titulo, categorias);
    }

    public void finalizarTransmisionEnCurso() {
        if (transmisionActual == null) {
            throw new IllegalStateException("No hay transmisión en curso");
        }
        transmisionActual.finalizar();
        transmisionesHistoricas.add(transmisionActual);
        transmisionActual = null;
    }

    public void recibirMuestraDeApoyo(Integer cantidad){
        if (cantidad == null || cantidad < 1 || cantidad > 10) {
            throw new IllegalArgumentException("La cantidad de la muestra de apoyo debe estar entre 1 y 10");
        }
        muestrasDeApoyo.add(cantidad);
    }

    public void suscribirse(Usuario usuario) {
        sucriptores.add(usuario);
    }

    public Transmision getTransmisionActual() {
        return transmisionActual;
    }

}