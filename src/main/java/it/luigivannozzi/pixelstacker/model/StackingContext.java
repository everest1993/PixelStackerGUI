package it.luigivannozzi.pixelstacker.model;

/*
 * STRATEGY DESIGN PATTERN
 * CONTEXT
 * Assegna i compiti agli oggetti Strategy. Genera una strategia adeguata al contesto
 */

import java.util.List;

public class StackingContext {

    private final List<String> imageList;
    private StackingStrategy stackingStrategy;

    public StackingContext(List<String> imageList, String mode) {
        this.imageList = imageList;
        setStrategy(mode);
    }

    // genera la strategia adeguata al contesto basandosi sul parametro String degli StackingController
    public void setStrategy(String mode) {
        String noiseMode = "Noise Stacking";
        String exposureMode = "Exposure Stacking";
        String focusMode = "Focus Stacking";

        if(mode.equals(noiseMode)) {
            stackingStrategy = new NoiseStrategy(imageList);
            System.out.println("Strategy impostato su Noise Stacking");
        }
        else if(mode.equals(focusMode)) {
            stackingStrategy = new FocusStrategy(imageList);
            System.out.println("Strategy impostato su Focus Stacking");
        }
        else if(mode.equals(exposureMode)) {
            stackingStrategy = new ExposureStrategy(imageList);
            System.out.println("Strategy impostato su Exposure Stacking");
        } else {
            throw new IllegalArgumentException("Mode non supportato: " + mode);
        }
    }

    public StackingStrategy getStrategy() {
        return stackingStrategy;
    }

    // chiama il metodo buildRunner della strategia concreta rispettiva per eseguire lo script corretto
    public ScriptRunner executeStrategy() {
        if (stackingStrategy == null) {
            throw new IllegalStateException("Strategia non impostata");
        }
        return stackingStrategy.buildRunner();
    }
}