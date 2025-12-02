package it.luigivannozzi.pixelstacker.model;

import it.luigivannozzi.pixelstacker.utils.Config;
import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ScriptRunnerOutputPathTest {

    @Test
    void usesConfiguredDirectory() throws Exception {
        File outDir = java.nio.file.Files.createTempDirectory("outDir").toFile();
        Config.getInstance().setOutputDir(outDir);

        String file = ScriptRunner.getOutputPathAndName();

        assertTrue(file.startsWith(outDir.getAbsolutePath()),
                "Il path deve iniziare con la directory configurata");
        assertTrue(file.endsWith(".tif"), "L'estensione deve essere .tif");
    }

    @Test
    void fallsBackToUserHome() {
        Config.getInstance().setOutputDir(null);

        String file = ScriptRunner.getOutputPathAndName();
        String home = System.getProperty("user.home");

        assertNotNull(Config.getInstance().getOutputDir(),
                "Il fallback deve settare automaticamente user.home come output dir");
        assertTrue(file.startsWith(home), "Senza config, il path deve ricadere in user.home");
        assertTrue(file.endsWith(".tif"), "L'estensione deve essere .tif");
    }
}