package it.luigivannozzi.pixelstacker.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.lang.reflect.Field;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void singletonInstance() {
        Config instance1 = Config.getInstance();
        Config instance2 = Config.getInstance();
        assertSame(instance1, instance2, "Config deve avere un'unica istanza");
    }

    @Test
    void setValidDirectoryPersistence() throws Exception {
        // reset Preferences
        Preferences.userNodeForPackage(Config.class).clear();
        Field f = Config.class.getDeclaredField("instance");
        f.setAccessible(true);
        // reset
        f.set(null, null);

        File tmpDir = java.nio.file.Files.createTempDirectory("outDir").toFile();

        Config c1 = Config.getInstance();
        c1.setOutputDir(tmpDir);
        assertEquals(tmpDir.getAbsolutePath(), c1.getOutputDir().getAbsolutePath());

        // reset
        f.set(null, null);
        Config c2 = Config.getInstance();
        assertEquals(tmpDir.getAbsolutePath(), c2.getOutputDir().getAbsolutePath(),
                "La directory salvata deve persistere tra le istanze");
    }

    @Test
    void setNullResetsToUserHome() throws Exception {
        Preferences.userNodeForPackage(Config.class).clear();
        Field f = Config.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        File dir = java.nio.file.Files.createTempDirectory("tempDir").toFile();

        Config cfg = Config.getInstance();
        cfg.setOutputDir(dir);
        assertNotNull(cfg.getOutputDir());

        // fallback a user.home dopo rimozione della preferenza
        cfg.setOutputDir(null);

        String home = new File(System.getProperty("user.home")).getAbsolutePath();
        assertEquals(home, cfg.getOutputDir().getAbsolutePath(),
                "Con null deve fare fallback alla user.home");

        f.set(null, null);
        Config cfg2 = Config.getInstance();
        assertEquals(home, cfg2.getOutputDir().getAbsolutePath());
    }

    @Test
    void invalidPathsAreIgnored() throws Exception {
        Preferences.userNodeForPackage(Config.class).clear();
        Field f = Config.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);

        Config cfg = Config.getInstance();
        String home = new File(System.getProperty("user.home")).getAbsolutePath();
        assertEquals(home, cfg.getOutputDir().getAbsolutePath(),
                "Senza preferenze, l'output di default è user.home");

        // prova a impostare un file come directory
        File tmpRoot = java.nio.file.Files.createTempDirectory("newDir").toFile();
        File tmpFile = java.nio.file.Files.createTempFile(tmpRoot.toPath(), "ps", ".tmp").toFile();
        cfg.setOutputDir(tmpFile);

        assertEquals(home, cfg.getOutputDir().getAbsolutePath(),
                "Un file non deve essere accettato; resta user.home");

        // directory inesistente
        File missing = new java.io.File(tmpRoot, "non_existing");
        cfg.setOutputDir(missing);
        assertEquals(home, cfg.getOutputDir().getAbsolutePath(),
                "Una directory inesistente non deve essere accettata; resta user.home");
    }

    @AfterEach
    void cleanup() throws Exception {
        Preferences.userNodeForPackage(Config.class).clear();
        Field f = Config.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);
    }
}