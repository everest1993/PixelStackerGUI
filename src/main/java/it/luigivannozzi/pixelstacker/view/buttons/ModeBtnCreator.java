package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è ModeBtn
 */

import javafx.scene.control.Button;

public class ModeBtnCreator extends BtnCreator {

    private final String title;
    private final String iconPath;

    public ModeBtnCreator(String title, String iconPath) {
        this.title = title;
        this.iconPath = iconPath;
    }

    @Override
    protected Button createButton() {
        return new ModeBtn(title, iconPath);
    }
}