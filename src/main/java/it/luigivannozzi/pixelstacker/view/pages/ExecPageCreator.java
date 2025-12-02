package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CONCRETE CREATOR
 * Il prodotto concreto è la pagina di elaborazione
 */

public class ExecPageCreator extends PageCreator {

    @Override
    public Page createPage() {
        return new ExecPage();
    }
}