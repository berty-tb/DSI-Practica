package org.example;

public class Mensaje {
    private Usuario autor;
    private String contenido;

    public Mensaje(Usuario autor, String contenido) {
        this.autor = autor;
        this.contenido = contenido;
    }
}
