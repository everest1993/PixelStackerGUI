package it.luigivannozzi.pixelstacker.observer;

/*
 * OBSERVER DESIGN PATTERN
 * Subject
 */

public interface ThemeSubject {

    void addObserver(ThemeObserver observer);
    void notifyObservers();
}