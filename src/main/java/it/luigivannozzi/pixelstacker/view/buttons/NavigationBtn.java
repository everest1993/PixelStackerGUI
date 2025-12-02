package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NavigationBtn extends Button {

    // pulsante per la navigazione verso pagine non dedicate allo stacking (settings e home)
    public NavigationBtn(String iconPath) {
        // stile
        getStyleClass().add("toolbar-button");

        // icona
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setPreserveRatio(true);

        int ICON_SIZE = 30;
        icon.setFitHeight(ICON_SIZE);
        icon.setFitWidth(ICON_SIZE);

        setGraphic(icon);

        int BTN_SIZE = 40;
        setMinSize(BTN_SIZE, BTN_SIZE);
        setMaxSize(BTN_SIZE, BTN_SIZE);
    }
}