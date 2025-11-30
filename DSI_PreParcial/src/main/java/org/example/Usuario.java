package org.example;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String userName;
    private Canal canal;
    private List<Canal> suscripciones = new ArrayList<>();

    public void suscribirseA(Canal unCanal) {
        if (suscripciones.contains(unCanal)) {
            throw new IllegalStateException("Ya estás suscrito a este canal");
        }
        suscripciones.add(unCanal);
        unCanal.suscribirse(this);
    }

}
