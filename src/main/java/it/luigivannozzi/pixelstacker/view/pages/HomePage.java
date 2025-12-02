package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

/*
 * OBSERVER DESIGN PATTERN
 * Concrete Observer
 */

import it.luigivannozzi.pixelstacker.controller.HomeController;
import it.luigivannozzi.pixelstacker.controller.SettingsController;
import it.luigivannozzi.pixelstacker.observer.ThemeObserver;
import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.view.SettingsPopUp;
import it.luigivannozzi.pixelstacker.view.buttons.ModeBtnCreator;
import it.luigivannozzi.pixelstacker.view.buttons.NavigationBtnCreator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import static it.luigivannozzi.pixelstacker.App.addSmoothHover;

// Classe concreta che implementa la pagina Home
public class HomePage extends Page implements ThemeObserver {

    // layout manager che posiziona tutti i nodi figli centrati in pila uno sopra l'altro (primo -> ultimo)
    private final StackPane root;

    private final HomeController controller;

    private final ImageView logo;

    public HomePage() {
        controller = new HomeController();

        String noisePath = "/images/Noise.png";
        String focusPath = "/images/Focus.png";
        String exposurePath = "/images/Exposure.png";

        // overlay per contenere il logo
        logo = new ImageView();

        // set del tema (ultimo tema salvato in Preferences)
        update();

        logo.setPreserveRatio(true);
        // costante per lo stile del logo della pagina Home
        double LOGO_HEIGHT = 150;
        logo.setFitHeight(LOGO_HEIGHT);
        StackPane.setAlignment(logo, Pos.TOP_LEFT);

        /*
         * FACTORY METHOD
         * pulsanti per selezionare la modalità
         */
        Button noiseButton = new ModeBtnCreator("Noise Stacking", noisePath)
                .create(_ -> controller.openNoiseStacking());

        Button focusButton = new ModeBtnCreator("Focus Stacking", focusPath)
                .create(_ -> controller.openFocusStacking());

        Button exposureButton = new ModeBtnCreator("Exposure Stacking", exposurePath)
                .create(_ -> controller.openExposureStacking());

        // zoom in hoover del cursore
        addSmoothHover(noiseButton, 1.1);
        addSmoothHover(focusButton, 1.1);
        addSmoothHover(exposureButton, 1.1);

        /*
         * OBSERVER DESIGN PATTERN
         * binding
         */
        Config.getInstance().addObserver(this);

        // regioni di spazio per distribuire i pulsanti delle stacking mode omogeneamente nell'HBox
        Region s1 = new Region();
        Region s2 = new Region();
        Region s3 = new Region();
        Region s4 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);
        HBox.setHgrow(s2, Priority.ALWAYS);
        HBox.setHgrow(s3, Priority.ALWAYS);
        HBox.setHgrow(s4, Priority.ALWAYS);

        // HBox è un layout manager che posiziona i nodi figli da sinistra verso destra, su una singola riga
        HBox stackingRow = new HBox(s1, noiseButton, s2, focusButton, s3, exposureButton, s4);
        stackingRow.setAlignment(Pos.CENTER);
        stackingRow.setSpacing(20);

        // path per il NavigationBtn della home
        String SETTINGS_PATH = "/icons/WhiteSettings.png";

        /*
         * FACTORY METHOD
         * pulsante per aprire le impostazioni
         */
        Button settingsButton = new NavigationBtnCreator(SETTINGS_PATH)
                .create(_ -> {});

        addSmoothHover(settingsButton, 1.1);
        SettingsPopUp settingsPopUp = new SettingsPopUp();
        SettingsController.getInstance().openSettings(settingsButton, settingsPopUp);

        StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT);
        StackPane.setMargin(settingsButton, new Insets(15));

        Label hint = new Label("Choose your stacking mode");
        StackPane.setAlignment(hint, Pos.BOTTOM_CENTER);
        StackPane.setMargin(hint, new Insets(0, 0, 50, 0));
        hint.getStyleClass().add("big-hint");

        root = new StackPane(logo, stackingRow, settingsButton, hint);
        root.getStyleClass().add("page-background");
    }

    // aggiorna il tema del logo
    @Override
    public void update() {
        String BLACK_LOGO_PATH = "/logos/BlackHomeLogo.png";
        String WHITE_LOGO_PATH = "/logos/WhiteHomeLogo.png";

        Config.Theme theme = Config.getInstance().getTheme();

        if(theme == Config.Theme.DARK) {
            logo.setImage(new Image(WHITE_LOGO_PATH));
        } else {
            logo.setImage(new Image(BLACK_LOGO_PATH));
        }
    }

    @Override
    public Parent getView() {
        return root;
    }
}