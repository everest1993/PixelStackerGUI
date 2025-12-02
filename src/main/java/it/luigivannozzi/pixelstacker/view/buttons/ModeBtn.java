package it.luigivannozzi.pixelstacker.view.buttons;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

// pulsante per la navigazione verso le varie pagine di stacking
public class ModeBtn extends Button {

    private final ImageView icon;

    public ModeBtn(String title, String iconPath) {

        icon = new ImageView(new Image(iconPath));
        icon.setPreserveRatio(true);
        icon.fitHeightProperty().bind(heightProperty().multiply(0.5));
        icon.fitWidthProperty().bind(icon.fitHeightProperty());

        // costanti per il dimensionamento del pulsante
        double MIN_SIZE = 200;
        double PREF_SIZE = 250;
        double MAX_SIZE = Double.MAX_VALUE;

        setMinSize(MIN_SIZE, MIN_SIZE);
        setPrefSize(PREF_SIZE, PREF_SIZE);
        setMaxSize(MAX_SIZE, MAX_SIZE);

        // stile
        getStyleClass().add("home-button");

        // titolo
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("mode-title");
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(MAX_SIZE);

        // contenitore interno
        VBox box = new VBox(icon, titleLabel);
        box.setAlignment(Pos.CENTER);
        setGraphic(box);
    }

    public void updateIcon(String iconPath) {
        icon.setImage(new Image(iconPath));
    }
}