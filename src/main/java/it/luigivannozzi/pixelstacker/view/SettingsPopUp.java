package it.luigivannozzi.pixelstacker.view;

import it.luigivannozzi.pixelstacker.controller.SettingsController;
import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.buttons.TextBtnCreator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;
import java.io.File;

public class SettingsPopUp {

    private final SettingsController controller;
    private final Label outputDirLabel;

    private final PopOver popOver;

    public SettingsPopUp() {
        controller = SettingsController.getInstance();
        controller.setPopUp(this);

        Config cfg = Config.getInstance();

        // titolo della finestra di impostazioni
        Label settingsLabel = new Label("Settings");
        settingsLabel.setPadding(new Insets(15, 0, 30, 0));
        BorderPane.setAlignment(settingsLabel, Pos.TOP_CENTER);
        settingsLabel.getStyleClass().add("settings-title");

        /*
         * FACTORY METHOD
         * pulsante per cambiare la cartella di output
         */
        Button selectDir = new TextBtnCreator("Select Output Folder")
                .create(_ -> controller.chooseOutputDirectory());

        // mostra la cartella selezionata (fallback user.home)
        File savedDir = controller.getOutputDir();
        String init = savedDir.getAbsolutePath();

        int LABEL_PADDING = 15;
        outputDirLabel = new Label();
        outputDirLabel.setPadding(new Insets(LABEL_PADDING));
        updateOutputDirLabel(init);

        // container centrale per pulsante e label
        VBox outputBox = new VBox(selectDir, outputDirLabel);
        outputBox.setAlignment(Pos.CENTER);

        // recupera la scena principale dell'applicazione
        Scene appScene = Router.getInstance().getWindow().getScene();

        // recupera l'ultimo tema salvato dall'utente
        boolean isDark = (cfg.getTheme() == Config.Theme.DARK);

        // toggle per il tema dell'applicazione
        ToggleSwitch themeBtn = new ToggleSwitch("Dark Mode");
        // stato iniziale
        themeBtn.setSelected(isDark);

        // on toggle
        themeBtn.selectedProperty().addListener((_, _, nowDark)
                -> cfg.switchAndApplyTheme(appScene, nowDark ? Config.Theme.DARK : Config.Theme.LIGHT));

        VBox themeBox = new VBox(themeBtn);
        themeBox.setAlignment(Pos.CENTER);
        themeBox.setPadding(new Insets(30));

        // contenitore delle varie sezioni
        VBox centerBox = new VBox(outputBox, themeBox);

        // layout principale del popup
        BorderPane popRoot = new BorderPane();
        popRoot.setTop(settingsLabel);
        popRoot.setCenter(centerBox);
        popRoot.setMinWidth(350);
        popRoot.setMinHeight(200);
        popRoot.getStyleClass().add("settings-popup");

        popOver = new PopOver(popRoot);
        popOver.setDetachable(false);
        popOver.setAutoHide(true);
        popOver.setArrowSize(0);
    }

    // viene invocato dal controller per mostrare la cartella selezionata
    public void updateOutputDirLabel(String path) {
        outputDirLabel.setText(path);
    }

    // funzione per mostrare il popUp
    public PopOver getView() {
        return popOver;
    }
}