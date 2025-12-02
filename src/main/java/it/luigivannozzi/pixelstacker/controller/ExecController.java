package it.luigivannozzi.pixelstacker.controller;

/*
 * SINGLETON DESIGN PATTERN
 */

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

// controller della pagina di esecuzione
public class ExecController implements Controller {

    private static ExecController instance;
    private ProgressBar bar;

    private Timeline running;

    private ExecController() {}

    public static ExecController getInstance() {
        if (instance == null) instance = new ExecController();
        return instance;
    }

    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }

    // metodo per iniziare il riempimento visivo della barra di progresso
    public void startSlowPrefill(double cap, Duration duration) {
        animateTo(cap, duration, null);
    }

    public void updateProgress(double fraction) {
        animateTo(fraction, Duration.millis(1000), null);
    }

    // metodo che permette di concatenare le animazioni
    public void stepThenPrefill(double stepTarget, Duration stepDur,
                                double prefillCap, Duration prefillDur) {
        animateTo(stepTarget, stepDur,
                () -> startSlowPrefill(prefillCap, prefillDur));
    }

    private void animateTo(double target, Duration duration, Runnable after) {
        if (bar == null) return;
        // clamp del valore target per evitare stati illegali
        double clamped = Math.max(0, Math.min(1, target));
        // durata animazione
        Duration dur = (duration == null ? Duration.millis(1000) : duration);

        Platform.runLater(() -> {
            // se è presente un'animazione in corso viene interrotta per evitare conflitti nel JavaFX Application Thread
            if (running != null) {
                running.stop();
                running = null;
            }

            running = new Timeline(
                    // il primo frame parte dal valore corrente di progresso
                    // Interpolator.EASE_BOTH permette la transizione fluida
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(bar.progressProperty(), bar.getProgress(), Interpolator.EASE_BOTH)),
                    // l'ultimo frame corrisponde al valore target
                    new KeyFrame(dur,
                            new KeyValue(bar.progressProperty(), clamped, Interpolator.EASE_BOTH))
            );
            running.setOnFinished(_ -> {
                running = null;
                if (after != null) after.run(); // chaining sicuro
            });
            // animazione
            running.play();
        });
    }

    public void finishAndNavigateHome(javafx.util.Duration hold, Runnable after) {
        // durata dell'animazione verso 1.0
        javafx.util.Duration animDur = javafx.util.Duration.millis(1000);

        animateTo(1.0, animDur, () -> {
            // pausa prima della navigazione
            javafx.animation.PauseTransition pause =
                    new javafx.animation.PauseTransition(hold != null ? hold : javafx.util.Duration.ZERO);
            pause.setOnFinished(_ -> { if (after != null) after.run(); });
            pause.play();
        });
    }
}