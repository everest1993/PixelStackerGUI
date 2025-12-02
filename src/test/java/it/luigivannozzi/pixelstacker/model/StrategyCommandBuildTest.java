package it.luigivannozzi.pixelstacker.model;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrategyCommandBuildTest {

    private static void assertCommandShape(List<String> cmd, String expectedMode, List<String> images) {
        assertTrue(cmd.size() >= 4 + images.size(), "Comando troppo corto: " + cmd);

        assertEquals(expectedMode, cmd.get(1), "Modalità errata");
        assertEquals("-o", cmd.get(2), "Manca -o");

        String outFile = cmd.get(3);
        assertNotNull(outFile, "Output file nullo");
        assertFalse(outFile.isBlank(), "Output file vuoto");

        List<String> tail = cmd.subList(4, cmd.size());
        assertEquals(images, tail, "La lista immagini nel comando non corrisponde");
    }

    @Test
    void noiseStrategyBuildsExpectedCommand() throws Exception {
        List<String> imgs = List.of("/tmp/A.ARW", "/tmp/B.ARW");
        ScriptRunner r = new NoiseStrategy(imgs).buildRunner();

        Field f = ScriptRunner.class.getDeclaredField("cmd");
        f.setAccessible(true);

        List<String> cmd = (List<String>) f.get(r);

        assertCommandShape(cmd, "noise", imgs);
    }

    @Test
    void focusStrategyBuildsExpectedCommand() throws Exception {
        List<String> imgs = List.of("/tmp/C.ARW", "/tmp/D.ARW");
        ScriptRunner r = new FocusStrategy(imgs).buildRunner();

        Field f = ScriptRunner.class.getDeclaredField("cmd");
        f.setAccessible(true);

        List<String> cmd = (List<String>) f.get(r);

        assertCommandShape(cmd, "focus", imgs);
    }

    @Test
    void exposureStrategyBuildsExpectedCommand() throws Exception {
        List<String> imgs = List.of("/tmp/E.ARW", "/tmp/F.ARW");
        ScriptRunner r = new ExposureStrategy(imgs).buildRunner();

        Field f = ScriptRunner.class.getDeclaredField("cmd");
        f.setAccessible(true);

        List<String> cmd = (List<String>) f.get(r);

        assertCommandShape(cmd, "exposure", imgs);
    }
}