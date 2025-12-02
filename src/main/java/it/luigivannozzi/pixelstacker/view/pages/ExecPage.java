package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

import it.luigivannozzi.pixelstacker.controller.ExecController;
import it.luigivannozzi.pixelstacker.utils.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// classe concreta che implementa la pagina di esecuzione
public class ExecPage extends Page {

    private final StackPane root;

    public ExecPage() {
        ExecController controller = ExecController.getInstance();

        String BLACK_LOGO_PATH = "/logos/BlackHomeLogo.png";
        String WHITE_LOGO_PATH = "/logos/WhiteHomeLogo.png";

        ImageView logo = new ImageView(new Image(
                (Config.getInstance().getTheme() == Config.Theme.DARK) ? WHITE_LOGO_PATH : BLACK_LOGO_PATH));
        logo.setPreserveRatio(true);

        // costante per lo stile del logo
        double LOGO_HEIGHT = 120;
        logo.setFitHeight(LOGO_HEIGHT);

        // come in HomePage
        StackPane.setAlignment(logo, Pos.TOP_LEFT);
        StackPane.setMargin(logo, new Insets(0));

        Label label = new Label("Stacking...");
        label.getStyleClass().add("page-title");

        ProgressBar progressBar = new ProgressBar(0);

        // binding per l'aggiornamento del progresso
        controller.setProgressBar(progressBar);

        // contenitore per la barra e la label
        VBox centralBox = new VBox(25, label, progressBar);
        centralBox.setAlignment(Pos.CENTER);
        centralBox.setFillWidth(true);

        Label hint = new Label("Please wait until the process is complete");
        StackPane.setAlignment(hint, Pos.BOTTOM_CENTER);
        StackPane.setMargin(hint, new Insets(0, 0, 150,0));
        hint.getStyleClass().add("big-hint");

        root = new StackPane();
        root.getStyleClass().add("page-background");
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(logo, centralBox, hint);

        // la dimensione della barra si adatta automaticamente alla finestra (80%)
        progressBar.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
        progressBar.setPrefHeight(40);
    }

    @Override
    public Parent getView() {
        return root;
    }
}