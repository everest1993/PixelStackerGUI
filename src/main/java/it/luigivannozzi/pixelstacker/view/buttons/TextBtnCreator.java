package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è SelectionBtn
 */

import javafx.scene.control.Button;

public class TextBtnCreator extends BtnCreator {

    private final String text;

    public TextBtnCreator(String text) {
        this.text = text;
    }

    @Override
    protected Button createButton() {
        return new TextBtn(text);
    }
}