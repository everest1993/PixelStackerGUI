package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è la pagina di apertura
 */

public class SplashPageCreator extends PageCreator {

    @Override
    public Page createPage() {
        return new SplashPage();
    }
}