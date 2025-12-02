package it.luigivannozzi.pixelstacker.view.buttons;

/*
* FACTORY METHOD
* CONCRETE PRODUCT
*/

import javafx.scene.control.Button;

// pulsante dotato di testo
public class TextBtn extends Button {

    public TextBtn(String text) {
        setText(text);
        getStyleClass().add("toolbar-button");
    }
}