package it.luigivannozzi.pixelstacker.utils;

/*
 * SINGLETON DESIGN PATTERN
 */

/*
 * OBSERVER DESIGN PATTERN
 * Concrete Subject
 */

import it.luigivannozzi.pixelstacker.observer.ThemeObserver;
import it.luigivannozzi.pixelstacker.observer.ThemeSubject;
import javafx.scene.Scene;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import static it.luigivannozzi.pixelstacker.App.DARK_CSS;
import static it.luigivannozzi.pixelstacker.App.LIGHT_CSS;

// classe per gestire e memorizzare le impostazioni selezionate dall'utente
public class Config implements ThemeSubject {

    private static Config instance;

    // persistenza cross-platform (registro su Windows, file XML su Linux/macOS)
    private final Preferences prefs;

    private File outputDir;
    private Theme currentTheme;

    private static final String OUTPUT_DIR = "outputdir";
    private static final String PREF_THEME_KEY = "theme";

    private final List<ThemeObserver> themeObservers;

    public enum Theme { LIGHT, DARK }

    private Config() {
        prefs = Preferences.userNodeForPackage(Config.class);

        themeObservers = new ArrayList<>();

        initOutputDir();

        currentTheme = initTheme();
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private void initOutputDir() {
        String saved = prefs.get(OUTPUT_DIR, null);
        if (saved != null && !saved.isBlank()) {
            File f = new File(saved);
            outputDir = (f.exists() && f.isDirectory())
                    ? f
                    : new File(System.getProperty("user.home"));
        } else {
            outputDir = new File(System.getProperty("user.home"));
        }
    }

    public void setOutputDir(File dir) {
        if (dir == null) {
            this.outputDir = new File(System.getProperty("user.home"));
            prefs.remove(OUTPUT_DIR);
        } else if (dir.exists() && dir.isDirectory()) {
            this.outputDir = dir;
            prefs.put(OUTPUT_DIR, dir.getAbsolutePath());
        }
    }

    public File getOutputDir() {
        return outputDir;
    }

    private Theme initTheme() {
        String v = prefs.get(PREF_THEME_KEY, Theme.LIGHT.name());
        return "DARK".equalsIgnoreCase(v) ? Theme.DARK : Theme.LIGHT;
    }

    public Theme getTheme() {
        return currentTheme;
    }

    // salva in Preferences e aggiorna il campo corrente
    public void setTheme(Theme theme) {
        if (theme == null) {
            return;
        }
        currentTheme = theme;
        prefs.put(PREF_THEME_KEY, theme.name());
    }

    // applica il tema richiesto alla scena
    public void applyTheme(Scene scene) {
        if (scene == null) {
            return;
        }
        var styles = scene.getStylesheets();
        styles.removeAll(LIGHT_CSS, DARK_CSS);
        styles.add(currentTheme == Theme.DARK ? DARK_CSS : LIGHT_CSS);
        notifyObservers();
    }

    // applica un tema specifico e lo salva
    public void switchAndApplyTheme(Scene scene, Theme theme) {
        setTheme(theme);
        applyTheme(scene);
    }

    @Override
    public void addObserver(ThemeObserver observer) {
        themeObservers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for(ThemeObserver observer : themeObservers) {
            observer.update();
        }
    }
}