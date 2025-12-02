package it.luigivannozzi.pixelstacker.controller;

/*
 * STRATEGY DESIGN PATTERN
 * CLIENT
 * Seleziona la strategia in relazione al contesto
 */

import it.luigivannozzi.pixelstacker.exceptions.MinimumImagesRequiredException;
import it.luigivannozzi.pixelstacker.model.ScriptRunner;
import it.luigivannozzi.pixelstacker.model.StackingContext;
import it.luigivannozzi.pixelstacker.utils.FilePicker;
import it.luigivannozzi.pixelstacker.utils.Router;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import it.luigivannozzi.pixelstacker.view.pages.ExecPage;
import it.luigivannozzi.pixelstacker.view.pages.HomePage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class StackingController implements Controller {

    private final String stackingMode;
    private final List<String> imageNamesList;
    private final List<String> imagePathsList;
    private final FilePicker filePicker;
    private File lastDirectory;
    private StackingContext context;

    public StackingController(String stackingMode) {
        this.stackingMode = stackingMode;
        imageNamesList = new ArrayList<>();
        imagePathsList = new ArrayList<>();
        filePicker = new FilePicker();
    }

    public String getStackingMode() {
        return stackingMode;
    }

    public List<String> getImageList() {
        return Collections.unmodifiableList(imageNamesList);
    }

    public List<String> getImagePaths() {
        return Collections.unmodifiableList(imagePathsList);
    }

    private void setContext(StackingContext context) {
        this.context = context;
    }

    private void setLastDirectory(File lastDirectory) {
        this.lastDirectory = lastDirectory;
    }

    private File getLastDirectory() {
        return lastDirectory;
    }

    public void goBack() {
        Router.getInstance().navigate(new HomePage());
    }

    // aggiunge un'immagine alla lista di stacking
    public void addImage() {
        // limite superiore di immagini importabili
        final int IMG_LIMIT = 250;

        File initialDir = getLastDirectory();
        setLastDirectory(Objects.requireNonNullElseGet(initialDir,
                () -> new File(System.getProperty("user.home"))));

        List<File> files = filePicker.pick(Router.getInstance().getWindow(), getLastDirectory());
        if (files == null || files.isEmpty()) return;

        for (File f : files) {
            String path = f.getAbsolutePath();
            if (!imagePathsList.contains(path) && imageNamesList.size() < IMG_LIMIT) {
                imageNamesList.add(f.getName());
                imagePathsList.add(path);
            }
        }

        // aggiorna la directory di partenza per il prossimo pick
        File parent = files.getFirst().getParentFile();
        if (parent != null && parent.isDirectory()) {
            setLastDirectory(parent);
        }
    }

    // rimuove un'immagine dalla lista di stacking
    public void removeImage(String name) {
        if (name == null) return;
        int idx = imageNamesList.indexOf(name);
        if (idx >= 0) {
            imageNamesList.remove(idx);
            imagePathsList.remove(idx);
        }
    }

    /*
     * STRATEGY DESIGN PATTERN
     * Implementazione della stacking strategy
     */
    // se il numero minimo di immagini richieste è soddisfatto genera un nuovo context concordemente alla
    // stacking mode ed esegue lo script relativo in un nuovo thread
    public void runStackingScript() throws MinimumImagesRequiredException {
        if (imagePathsList.size() < 2) {
            throw new MinimumImagesRequiredException();
        }

        setContext(new StackingContext(getImagePaths(), getStackingMode()));
        ScriptRunner runner = context.executeStrategy();

        // navigazione verso la pagina di esecuzione
        Router.getInstance().navigate(new ExecPage());

        // viene eseguito un nuovo thread per non congelare la GUI durante l'elaborazione
        new Thread(() -> {
            try {
                runner.runStacking();
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // binding alla pagine chiamante
                    alert.initOwner(Router.getInstance().getWindow());
                    // blocca” l’interazione con la finestra principale finché non viene chiusa
                    alert.initModality(javafx.stage.Modality.WINDOW_MODAL);
                    alert.setTitle("The operation could not be completed.");
                    alert.setHeaderText("Stacking failed.");
                    alert.setContentText(
                            (e.getMessage() != null && !e.getMessage().isBlank())
                                    ? e.getMessage()
                                    : "An unexpected error occurred during stacking."
                    );
                    alert.setResizable(false);
                    alert.showAndWait();
                    // navigazione verso la home
                    Router.getInstance().navigate(new HomePage());
                });
            }
        }, "stacking-runner").start();
    }
}