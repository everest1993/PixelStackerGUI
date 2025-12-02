package it.luigivannozzi.pixelstacker.controller;

/*
 * SINGLETON DESIGN PATTERN
 */

import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.utils.DirectoryPicker;
import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.SettingsPopUp;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;

import java.io.File;

public class SettingsController implements Controller {

    private static SettingsController instance;

    private SettingsPopUp popUp;
    private final DirectoryPicker directoryPicker;

    // classe concreta per la selezione delle impostazioni
    // permette all'utente di selezionare la cartella di output
    private SettingsController() {
        directoryPicker = new DirectoryPicker();
    }

    // seleziona la cartella di output attraverso un picker e propaga la preferenza alla classe Config
    public void chooseOutputDirectory() {
        File current = Config.getInstance().getOutputDir();
        File chosen = directoryPicker.pick(Router.getInstance().getWindow(), current);
        if (chosen != null) {
            Config.getInstance().setOutputDir(chosen);

            popUp.updateOutputDirLabel(chosen.getAbsolutePath());
        }
    }

    // recupera le preferenze utente dalla classe Config
    public File getOutputDir() {
        return Config.getInstance().getOutputDir();
    }

    public void setPopUp(SettingsPopUp popUp) {
        this.popUp = popUp;
    }

    // apertura popup di impostazioni
    public void openSettings(Button settingsButton, SettingsPopUp popUp) {
        settingsButton.setOnAction(_ -> {
            var pop = popUp.getView();
            if (pop.isShowing()) {
                pop.hide();
            } else {
                pop.show(settingsButton);
                Platform.runLater(() -> {
                    Bounds b = settingsButton.localToScreen(settingsButton.getBoundsInLocal());
                    // sposta a sinistra di x pixel
                    double offsetX = -155;
                    double x = b.getMinX() + b.getWidth()/2 - pop.getWidth()/2 + offsetX;
                    // sposta in alto di x pixel
                    double offsetY = 5;
                    double y = b.getMaxY() + offsetY;

                    pop.setX(Math.round(x));
                    pop.setY(Math.round(y));
                });
            }
        });
    }

    public static SettingsController getInstance() {
        if (instance == null) {
            instance = new SettingsController();
        }
        return instance;
    }
}