package it.luigivannozzi.pixelstacker.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.util.List;

// classe di utility per selezionare file di immagini (formati RAW)
public class FilePicker {
    public List<File> pick(Window owner, File initialDir) {
        FileChooser chooser = new FileChooser();

        chooser.setTitle("Select Images");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "RAW images",
                "*.arw","*.ARW",
                "*.nef","*.NEF",
                "*.cr2","*.CR2",
                "*.cr3","*.CR3",
                "*.rw2","*.RW2",
                "*.orf","*.ORF",
                "*.raf","*.RAF",
                "*.dng","*.DNG"
        ));

        if (initialDir != null && initialDir.exists() && initialDir.isDirectory()) {
            chooser.setInitialDirectory(initialDir);
        }
        // permette selezione multipla
        return chooser.showOpenMultipleDialog(owner);
    }
}