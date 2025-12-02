package it.luigivannozzi.pixelstacker.exceptions;

// eccezione che viene lanciata se l'utente prova ad avviare l'elaborazione con una sola immagine
public class MinimumImagesRequiredException extends Exception {

    public MinimumImagesRequiredException() {
        super("Select at least 2 images to start stacking.");
    }
}