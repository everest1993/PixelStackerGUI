package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CREATOR
 * Nell'applicazione sono attualmente previste le seguenti tipologie di pulsanti: il primo tipo serve a selezionare la
 * modalità di stacking, il secondo tipo serve per navigare verso la pagina Home o verso la pagina Settings, il terzo tipo
 * serve per selezionare la cartella di output o per aggiungere o rimuovere un'immagine dalla lista
 */

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class BtnCreator {

    // crea il pulsante e assegna l'operazione tramite gestore di eventi
    public Button create(EventHandler<ActionEvent> onAction) {
        Button b = createButton();

        if(onAction != null) {
            // metodo Button JavaFX
            b.setOnAction(onAction);
        }

        return b;
    }

    // factory method
    protected abstract Button createButton();
}