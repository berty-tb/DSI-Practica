package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transmision {
    private Integer maxViewers = 0;
    private Integer viewersActuales = 0;
    private List<Usuario> viewers = new ArrayList<>();
    private String titulo;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private List<Mensaje> chat = new ArrayList<>();
    private List<String> categorias;

    public Transmision(String titulo, List<String> categorias) {
        this.titulo = titulo;
        this.categorias = categorias;
        this.inicio = LocalDateTime.now();
    }

    public void finalizar() {
        if (this.inicio == null) {
            throw new IllegalStateException("La transmisión no ha iniciado");
        }
        this.fin = LocalDateTime.now();
    }

    public void serVistaPor(Usuario usuario) {
        viewersActuales++;
        this.viewers.add(usuario);
        verificarMaximosViewers();
    }

    private void verificarMaximosViewers() {
        if (viewersActuales > maxViewers) {
            maxViewers = viewersActuales;
        }
    }

    public void dejarDeVer(Usuario usuario) {
        viewersActuales--;
        viewers.remove(usuario);
    }

    public void chatear(Mensaje mensaje) {
        chat.add(mensaje);
    }

    public List<Mensaje> verChat() {
        return chat;
    }
}
