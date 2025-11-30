package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoCanales {
    private static RepoCanales instance = new RepoCanales();
    private List<Canal> canales = new ArrayList<>();

    public void agregarCanal(Canal canal) {
        canales.add(canal);
    }

    public List<Canal> listarCanales() {
        return new ArrayList<>(canales);
    }

    public List<Transmision> listarTransmisionesEnCurso() {
        return canales.stream()
                .map(Canal::getTransmisionActual)
                .filter(t -> t != null)
                .collect(Collectors.toList());
    }

    public static RepoCanales getInstance() {
        return instance;
    }
}
