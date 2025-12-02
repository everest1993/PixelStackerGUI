package it.luigivannozzi.pixelstacker.utils;

/*
 * SINGLETON DESIGN PATTERN
 */

import it.luigivannozzi.pixelstacker.controller.*;
import it.luigivannozzi.pixelstacker.view.pages.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

// classe singleton per la centralizzazione della navigazione
public class Router {

    /*
     * SINGLETON DESIGN PATTERN
     * campo private static per lo storing del singleton
     */
    private static Router instance;

    private final Stage stage;

    /*
     * SINGLETON DESIGN PATTERN
     * Costruttore privato. L'istanza deve essere recuperata attraverso l'apposito metodo
     */
    private Router(Stage stage) {
        this.stage = stage;
    }

    // imposta la view relativa alla pagina corrispondente nello stage JavaFX
    public void showPage(Page page) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(page.getView()));
        } else {
            stage.getScene().setRoot(page.getView());
        }
    }

    // metodo di navigazione verso una pagina generica
    public void navigate(Page page) {
        showPage(page);
    }

    // le classi di utility picker hanno bisogno di un riferimento a una finestra come owner
    // il metodo serve al corretto istanziamento e alla corretta visualizzazione (sopra owner) dei chooser JavaFX
    public Stage getWindow() {
        return this.stage;
    }

    // metodo init
    public static void setRouter(Stage stage) {
        if (instance == null) {
            instance = new Router(stage);
        }
    }

    /*
     * SINGLETON DESIGN PATTERN
     * Metodo per ottenere l'istanza
     */
    public static Router getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Router non inizializzato.");
        }
        return instance;
    }
}