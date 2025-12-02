package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * ABSTRACT PRODUCT
 */

// classe astratta da cui derivano tutti i contenitori grafici di JavaFX
import javafx.scene.Parent;

// interfaccia da cui ereditano tutti i tipi di pagina
public abstract class Page {
    // metodo astratto che, una volta implementato, restituisce il nodo root della pagina
    public abstract Parent getView();
}