package it.luigivannozzi.pixelstacker.view.pages;

/*
 * FACTORY METHOD
 * CREATOR
 * Nell'applicazione sono presenti diverse tipologie di pagina: SplashPage HomePage, SettingsPage,
 * StackingPage (una per tipo), UpcomingPage ed ExecPage
 */

// creatore che espone il Factory Method
public abstract class PageCreator {
    public abstract Page createPage();
}