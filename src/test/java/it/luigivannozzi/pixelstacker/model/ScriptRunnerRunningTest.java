package it.luigivannozzi.pixelstacker.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScriptRunnerRunningTest {

    @Test
    void runStackingFail() {
        // simulazione output processo
        List<String> command = new ArrayList<>();

        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")) {
            command = List.of("cmd.exe", "/c", "echo jhdcqlknc && exit 2");
        }
        else {
            command = List.of("/bin/sh", "-c", "echo jhdcqlknc && exit 2");
        }
        ScriptRunner r = new ScriptRunner(command);

        RuntimeException ex = assertThrows(RuntimeException.class, r::runStacking);
    }
}