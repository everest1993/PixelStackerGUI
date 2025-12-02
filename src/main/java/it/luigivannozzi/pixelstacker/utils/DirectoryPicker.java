package it.luigivannozzi.pixelstacker.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import java.io.File;

// classe di utility per selezionare una cartella
public class DirectoryPicker {
    public File pick(Window owner, File dir) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select output folder");
        if (dir != null && dir.exists() && dir.isDirectory()) {
            chooser.setInitialDirectory(dir);
        }
        // selezione singola
        return chooser.showDialog(owner);
    }
}