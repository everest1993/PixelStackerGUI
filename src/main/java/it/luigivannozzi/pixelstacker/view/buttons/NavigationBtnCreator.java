package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è NavigationBtn
 */

import javafx.scene.control.Button;

public class NavigationBtnCreator extends BtnCreator {

    private final String iconPath;

    public NavigationBtnCreator(String iconPath) {
        this.iconPath = iconPath;
    }

    @Override
    protected Button createButton() {
        return new NavigationBtn(iconPath);
    }
}