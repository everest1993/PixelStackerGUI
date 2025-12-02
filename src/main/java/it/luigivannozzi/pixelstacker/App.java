package it.luigivannozzi.pixelstacker;

import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.pages.HomePage;
import it.luigivannozzi.pixelstacker.view.pages.SplashPage;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
// finestra JavaFX
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
// contenuto finestra
import javafx.scene.Scene;
import javafx.util.Duration;

/*
 * Application è la classe base fornita da JavaFX per gestire il ciclo di vita di un'applicazione.
 * La classe App deve estendere Application per ereditare i metodi necessari al funzionamento (start).
 */
public class App extends Application {

    // fogli di stile
    public static final String BASE_CSS  = "/css/base.css";
    public static final String LIGHT_CSS = "/css/light.css";
    public static final String DARK_CSS  = "/css/dark.css";

    // il metodo start crea la finestra e aggiunge nodi alla scena. Il metodo stop è opzionale
    @Override
    public void start(Stage stage) {

        // genera e configura il Router di navigazione
        Router.setRouter(stage);
        // genera e configura la configurazione delle preferenze utente
        Config cfg = Config.getInstance();

        // la prima pagina a essere mostrata è la Splash
        Parent splash = new SplashPage().getView();

        Scene scene = new Scene(new StackPane(splash));
        scene.getStylesheets().add(BASE_CSS);
        // applica l'ultimo tema selezionato dall'utente
        cfg.applyTheme(scene);

        // adatta il colore della transizione al tema
        scene.setFill(cfg.getTheme() == Config.Theme.DARK ? Color.BLACK : Color.WHITE);

        stage.setScene(scene);

        // titolo della finestra
        stage.setTitle("PixelStacker");

        // calcola le dimensioni dello schermo
        double screenW = javafx.stage.Screen.getPrimary().getBounds().getWidth();
        double screenH = javafx.stage.Screen.getPrimary().getBounds().getHeight();

        // costante per regolare le dimensioni della finestra al lancio (non full screen)
        double RATIO = 2;

        // set delle dimensioni al lancio della finestra
        stage.setWidth(screenW / RATIO);
        stage.setHeight(screenH / RATIO);

        // costante per impostare la larghezza minima della finestra
        int MIN_WIDTH = 1000;
        stage.setMinWidth(MIN_WIDTH);
        // costante per impostare l'altezza minima della finestra
        int MIN_HEIGHT = 600;
        // set delle dimensioni minime della finestra (non riducibile al di sotto per consistenza grafica)
        stage.setMinHeight(MIN_HEIGHT);

        // mostra la finestra
        stage.show();

        // animazione di fade
        getPauseTransition(splash, scene).play();
    }

    private PauseTransition getPauseTransition(Parent splash, Scene scene) {
        double TRANSITION_DURATION = 1;

        FadeTransition out = new FadeTransition(Duration.seconds(TRANSITION_DURATION), splash);
        out.setFromValue(1);
        out.setToValue(0);

        out.setOnFinished(_ -> {
            // crea una nuova HomePage
            Parent newHome = new HomePage().getView();
            newHome.setOpacity(0);
            scene.setRoot(newHome);

            FadeTransition in = new FadeTransition(Duration.seconds(TRANSITION_DURATION), newHome);
            in.setFromValue(0);
            in.setToValue(1);
            in.play();
        });

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(_ -> out.play());
        return delay;
    }

    // zooma le icone delle modalità in hoover del cursore
    public static void addSmoothHover(Parent btn, double scale) {
        btn.setOnMouseEntered(_ -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(scale);
            st.setToY(scale);
            st.play();
        });

        btn.setOnMouseExited(_ -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
}