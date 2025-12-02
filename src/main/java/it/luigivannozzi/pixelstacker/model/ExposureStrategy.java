package it.luigivannozzi.pixelstacker.model;

/*
 * STRATEGY DESIGN PATTERN
 * CONCRETE STRATEGY
 * Strategia concreta per invocare l'algoritmo di Exposure Stacking
 */

import java.util.ArrayList;
import java.util.List;

public class ExposureStrategy implements StackingStrategy {

    private final List<String> imageList;

    public ExposureStrategy(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public ScriptRunner buildRunner() {
        String exe = ScriptRunner.getExecutable();
        String outFile = ScriptRunner.getOutputPathAndName();

        List<String> cmd = new ArrayList<>();
        cmd.add(exe);
        cmd.add("exposure");
        cmd.add("-o");
        cmd.add(outFile);
        cmd.addAll(imageList);

        return new ScriptRunner(cmd);
    }
}