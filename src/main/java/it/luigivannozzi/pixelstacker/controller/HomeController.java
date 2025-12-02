package it.luigivannozzi.pixelstacker.controller;

import it.luigivannozzi.pixelstacker.utils.Router;
import it.luigivannozzi.pixelstacker.view.pages.StackingPageCreator;

// classe concreta che implementa la navigazione nella pagina Home
public class HomeController implements Controller {

    // navigazione verso la pagina di Noise Stacking
    public void openNoiseStacking()   {
        Router.getInstance().navigate(new StackingPageCreator
                (new StackingController("Noise Stacking")).createPage());
    }

    // navigazione verso la pagina di Focus Stacking
    public void openFocusStacking()   {
        Router.getInstance().navigate(new StackingPageCreator
                (new StackingController("Focus Stacking")).createPage());
    }

    // navigazione verso la pagina di Exposure Stacking
    public void openExposureStacking() {
        Router.getInstance().navigate(new StackingPageCreator
                (new StackingController("Exposure Stacking")).createPage());
    }
}