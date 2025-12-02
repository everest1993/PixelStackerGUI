package it.luigivannozzi.pixelstacker.model;

/*
 * STRATEGY DESIGN PATTERN
 * STRATEGY INTERFACE
 * Specifica i metodi che tutte le strategie devono implementare
 */

public interface StackingStrategy {
    ScriptRunner buildRunner();
}