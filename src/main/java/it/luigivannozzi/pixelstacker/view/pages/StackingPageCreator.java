package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è una pagina di stacking
 */

import it.luigivannozzi.pixelstacker.controller.StackingController;

public class StackingPageCreator extends PageCreator {

    private final StackingController controller;

    public StackingPageCreator(StackingController controller) {
        this.controller = controller;
    }

    @Override
    public Page createPage() {
        return new StackingPage(controller);
    }
}