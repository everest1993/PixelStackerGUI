package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è la HomePage
 */

public class HomePageCreator extends PageCreator {

    @Override
    public Page createPage() {
        return new HomePage();
    }
}