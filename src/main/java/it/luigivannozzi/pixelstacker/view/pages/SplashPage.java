package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

import it.luigivannozzi.pixelstacker.utils.Config;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

// classe concreta che implementa la pagina di apertura
public class SplashPage extends Page {

    private final StackPane root;

    public SplashPage() {

        String WHITE_LOGO = "/logos/WhiteSplashLogo.png";
        String BLACK_LOGO = "/logos/BlackSplashLogo.png";

        String logoPath = Config.getInstance().getTheme() == Config.Theme.LIGHT ? BLACK_LOGO : WHITE_LOGO;
        // logo
        ImageView logo = new ImageView(new Image(logoPath));
        logo.setPreserveRatio(true);
        logo.setFitWidth(300);

        // aggiunge il logo alla root
        root = new StackPane(logo);
        root.getStyleClass().add("page-background");
    }

    @Override
    public Parent getView() {
        return root;
    }
}