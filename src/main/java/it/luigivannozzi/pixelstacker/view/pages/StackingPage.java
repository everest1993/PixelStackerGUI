package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE PRODUCT
 */

import it.luigivannozzi.pixelstacker.controller.SettingsController;
import it.luigivannozzi.pixelstacker.controller.StackingController;
import it.luigivannozzi.pixelstacker.exceptions.MinimumImagesRequiredException;
import it.luigivannozzi.pixelstacker.observer.ThemeObserver;
import it.luigivannozzi.pixelstacker.utils.Config;
import it.luigivannozzi.pixelstacker.utils.RawPreviewLoader;
import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.SettingsPopUp;
import it.luigivannozzi.pixelstacker.view.buttons.TextBtnCreator;
import it.luigivannozzi.pixelstacker.view.buttons.NavigationBtnCreator;
import javafx.collections.FXCollections;
// rende reattiva la ListView di JavaFX
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.util.*;

import static it.luigivannozzi.pixelstacker.App.addSmoothHover;

// classe concreta che implementa le varie stacking pages
public class StackingPage extends Page implements ThemeObserver {

    private final StackingController controller;
    private final String stackingMode;

    // ObservableArrayList è una lista JavaFX che notifica automaticamente gli osservatori quando vengono
    // effettuati cambiamenti
    private final ObservableList<String> images = FXCollections.observableArrayList();

    private ListView<String> listView;
    private HBox previewPane;

    private ImageView preview;
    private ImageView placeholder;

    private VBox root;

    public StackingPage(StackingController controller) {
        this.controller = controller;
        stackingMode = controller.getStackingMode();

        if(stackingMode.equals("Exposure Stacking") ||  stackingMode.equals("Focus Stacking")) {
            renderUpcomingPage();
        } else {
            renderStackingPage();
        }
    }

    private void renderStackingPage() {
        Config.getInstance().addObserver(this);

        this.preview = new ImageView();

        // placeholder (fallback se non c'è un'immagine selezionata)
        placeholder = new ImageView();
        placeholder.setPreserveRatio(true);
        placeholder.setFitWidth(400);

        /*
         * FACTORY METHOD
         * pulsante per tornare alla home
         */
        String HOME_PATH = "/icons/WhiteHomeIcon.png";
        Button homeBtn = new NavigationBtnCreator(HOME_PATH).create(_ -> controller.goBack());
        addSmoothHover(homeBtn, 1.1);

        // titolo della pagina passato come parametro a run time concordemente con il controller
        Label titleLbl = new Label(stackingMode);
        titleLbl.getStyleClass().add("page-title");

        // generazione della top bar
        StackPane topBar = new StackPane(homeBtn, titleLbl, generateSettingsButton());
        StackPane.setAlignment(titleLbl, Pos.CENTER);
        StackPane.setAlignment(homeBtn, Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));

        VBox leftPane = buildLeftSection();
        VBox rightPane = buildRightSection();

        update();

        // SplitPane è un contenitore che divide lo spazio tra più pannelli con uno o più divisori trascinabili
        SplitPane split = new SplitPane(leftPane, rightPane);
        split.setDividerPositions(0.32);

        root = new VBox(topBar, split);
        VBox.setVgrow(split, Priority.ALWAYS);
        root.getStyleClass().add("page-background");

        refreshImageList();
        viewList(listView, previewPane);
    }

    private void renderUpcomingPage() {
        // label che mostra la stacking mode (focus e exposure)
        Label stackingMode = new Label(controller.getStackingMode());
        stackingMode.getStyleClass().add("page-title");

        // label che mostra lo stato di placeholder della pagina
        Label upcomingLabel = new Label("Coming soon...");
        upcomingLabel.getStyleClass().add("mode-title");

        String HOME_PATH = "/icons/WhiteHomeIcon.png";

        /*
         * FACTORY METHOD
         * pulsante per tornare alla home
         */
        Button homeBtn = new NavigationBtnCreator(HOME_PATH).create(_ -> controller.goBack());
        addSmoothHover(homeBtn, 1.1);

        // nodo contenitore che impila gli elementi al suo interno
        StackPane pane = new StackPane(homeBtn, stackingMode, generateSettingsButton(), upcomingLabel);
        StackPane.setAlignment(homeBtn, Pos.TOP_LEFT);
        StackPane.setAlignment(stackingMode, Pos.TOP_CENTER);
        StackPane.setAlignment(upcomingLabel, Pos.CENTER);
        pane.setPadding(new Insets(15));

        root = new VBox(pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        root.getStyleClass().add("page-background");
    }

    // costruisce la parte sinistra della pagina, dove sono presenti la lista di immagini e il pulsante add
    private VBox buildLeftSection() {
        // lista ordinata per nome delle immagini
        SortedList<String> sorted = new SortedList<>(images, Comparator.comparing
                (s -> s.toLowerCase(Locale.ROOT)));
        // listview è un elemento JavaFX che mostra una lista verticale di oggetti
        // viene alimentata dalla sorted list
        ListView<String> listView = new ListView<>(sorted);
        // permette alla lista di occupare tutto lo spazio a disposizione
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.getStyleClass().add("list-background");
        this.listView = listView;

        // metodo che permette di definire come vengono create e renderizzate le celle della lista
        listView.setCellFactory(_ -> new ListCell<>() {
            private final Label nameLbl = new Label();

            /*
             * FACTORY METHOD
             * pulsante per rimuovere una foto dalla lista
             */
            private final Button del = new TextBtnCreator("-").create(_ -> {
                String item = getItem();
                if (item != null) {
                    controller.removeImage(item);
                    refreshImageList();
                }});

            // spacer per spostare il pulsante verso destra
            private final Region spacer = new Region();
            private final HBox box = new HBox(nameLbl, spacer, del);

            {
                // aggiunge lo stile al pulsante per rimuovere un'immagine
                del.getStyleClass().add("delete-button");
                // lo spacer occupa tutto lo spazio disponibile
                HBox.setHgrow(spacer, Priority.ALWAYS);
                // uso solo il graphic nella cella
                setText(null);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                nameLbl.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nameLbl, Priority.ALWAYS);
                nameLbl.setWrapText(false);
                nameLbl.setTextOverrun(OverrunStyle.ELLIPSIS);

                // click su cella vuota mostra placeholder
                addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> {
                    if (isEmpty()) {
                        listView.getSelectionModel().clearSelection();
                        previewPane.getChildren().setAll(placeholder);
                        ev.consume();
                    }
                });
            }

            // metodo che aggiorna le righe. updateItem(item, empty) viene chiamato automaticamente
            // per le celle visibili ogni volta che cambia il contenuto della lista o la cella viene
            // riutilizzata per un altro elemento.
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    nameLbl.setText(item);
                    setGraphic(box);
                }
            }
        });

        Label hint = new Label("Add two or more images");
        hint.getStyleClass().add("small-hint");

        HBox hintBox = new HBox(hint);
        hintBox.setPadding(new Insets(15));
        hintBox.setAlignment(Pos.CENTER);

        /*
         * FACTORY METHOD
         * pulsante per aggiungere una o più foto alla lista
         */
        Button addBtn = new TextBtnCreator("+").create(_ -> {
            controller.addImage();
            refreshImageList();});
        addBtn.getStyleClass().add("add-button");
        addBtn.setMaxWidth(Double.MAX_VALUE);

        addSmoothHover(addBtn, 1.01);

        // parte sinistra della finestra (lista e pulsante per aggiungere immagini)
        VBox leftPane = new VBox(5, listView, hintBox, addBtn);
        leftPane.setPadding(new Insets(10));
        leftPane.setPrefWidth(220);
        leftPane.setMinWidth(200);
        leftPane.getStyleClass().add("list-background");

        return leftPane;
    }

    // costruisce la parte destra della pagina con la preview dell'immagine selezionata e il pulsante stack
    private VBox buildRightSection() {
        // contenitore della preview
        // viene selezionato il placeholder di default
        HBox previewPane = new HBox(placeholder);
        previewPane.setAlignment(Pos.CENTER);
        previewPane.setMaxHeight(Double.MAX_VALUE);
        // permette il ridimensionamento della preview
        previewPane.setMinSize(200, 0);
        previewPane.getStyleClass().add("placeholder-background");
        this.previewPane = previewPane;

        /*
         * FACTORY METHOD
         * pulsante avviare lo script di stacking
         */
        Button stackBtn = new TextBtnCreator("Stack").create(_ ->
        {
            try {
                controller.runStackingScript();
            } catch (MinimumImagesRequiredException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // binding alla pagina chiamante per corretta visualizzazione
                alert.initOwner(Router.getInstance().getWindow());
                // blocca” l’interazione con la finestra principale finché non viene chiusa
                alert.initModality(javafx.stage.Modality.WINDOW_MODAL);
                alert.setTitle("The operation could not be completed.");
                alert.setHeaderText("Insufficient number of images.");
                alert.setContentText(ex.getMessage());
                alert.setResizable(false);
                alert.showAndWait();
            }
        });

        addSmoothHover(stackBtn, 1.01);

        stackBtn.getStyleClass().add("stack-button");
        stackBtn.setMaxWidth(Double.MAX_VALUE);

        // regione di spazio per lo stack button
        HBox bottomRegion = new HBox(stackBtn);
        HBox.setHgrow(stackBtn, Priority.ALWAYS);
        bottomRegion.setAlignment(Pos.CENTER);
        bottomRegion.setPadding(new Insets(10, 15, 10, 15));

        // parte destra della finestra (preview e stack button)
        VBox rightPane = new VBox(previewPane, bottomRegion);
        VBox.setVgrow(previewPane, Priority.ALWAYS);

        // permette il corretto ridimensionamento della preview
        rightPane.setMinSize(400, 0);
        rightPane.getStyleClass().add("placeholder-background");

        // dimensionamento della preview
        preview.setPreserveRatio(true);
        preview.fitWidthProperty().bind(previewPane.widthProperty());
        preview.fitHeightProperty().bind(previewPane.heightProperty());

        return rightPane;
    }

    private void viewList(ListView<String> listView, HBox previewPane) {
        // permette di visualizzare l'anteprima di un'immagine selezionata
        listView.getSelectionModel().selectedIndexProperty()
                .addListener((_, _, newI) -> {
                    if (newI == null || newI.intValue() < 0) {
                        previewPane.getChildren().setAll(placeholder);
                        return;
                    }
                    String selectedName = listView.getItems().get(newI.intValue());
                    List<String> names = controller.getImageList();
                    List<String> paths = controller.getImagePaths();
                    // binding listView - lista immagini del controller
                    int idx = names.indexOf(selectedName);
                    if (idx < 0 || idx >= paths.size()) {
                        previewPane.getChildren().setAll(placeholder);
                        return;
                    }
                    String path = paths.get(idx);
                    Image img = previewFromPath(path);
                    if (img == null || img.isError()) {
                        previewPane.getChildren().setAll(placeholder);
                    } else {
                        preview.setImage(img);
                        previewPane.getChildren().setAll(preview);
                    }
                });
    }

    // aggiorna la lista delle immagini aggiunte per lo stacking
    private void refreshImageList() {
        images.clear();

        List<String> names = controller.getImageList();
        List<String> paths = controller.getImagePaths();

        int n = Math.min(names.size(), paths.size());
        for (int i = 0; i < n; i++) {
            images.add(names.get(i));
        }
    }

    // utilizza la classe RawPreviewLoader per generare la preview di un'immagine RAW passata come argomento
    private Image previewFromPath(String path) {
        try {
            java.nio.file.Path p = java.nio.file.Paths.get(path);
            return RawPreviewLoader.load(p);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Button generateSettingsButton() {
        String SETTINGS_PATH = "/icons/WhiteSettings.png";

        /*
         * FACTORY METHOD
         * pulsante per aprire le impostazioni
         */
        // path per il NavigationBtn della home
        Button settingsButton = new NavigationBtnCreator(SETTINGS_PATH)
                .create(_ -> {});

        addSmoothHover(settingsButton, 1.1);
        SettingsPopUp settingsPopUp = new SettingsPopUp();
        SettingsController.getInstance().openSettings(settingsButton, settingsPopUp);

        StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT);

        return settingsButton;
    }

    @Override
    public void update() {
        String BLACK_PLACEHOLDER = "/icons/BlackPlaceholder.png";
        String WHITE_PLACEHOLDER = "/icons/WhitePlaceholder.png";

        Config.Theme theme = Config.getInstance().getTheme();

        if(theme == Config.Theme.DARK) {
            placeholder.setImage(new Image(WHITE_PLACEHOLDER));
        } else {
            placeholder.setImage(new Image(BLACK_PLACEHOLDER));
        }
    }

    @Override
    public Parent getView() {
        return root;
    }
}