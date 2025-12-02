package it.luigivannozzi.pixelstacker.controller;

import it.luigivannozzi.pixelstacker.exceptions.MinimumImagesRequiredException;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StackingControllerTest {

    @Test
    void getModeReflectsArgument() {
        StackingController c = new StackingController("Noise Stacking");
        assertEquals("Noise Stacking", c.getStackingMode());
    }

    @Test
    void removeImageRemovesNameAndPath() throws Exception {
        StackingController c = new StackingController("Noise Stacking");
        Field imageNames = c.getClass().getDeclaredField("imageNamesList");
        Field imagePaths = c.getClass().getDeclaredField("imagePathsList");
        imageNames.setAccessible(true);
        imagePaths.setAccessible(true);

        imageNames.set(c, new ArrayList<>(List.of("image1", "image2")));
        imagePaths.set(c, new ArrayList<>(List.of("image1Path", "image2Path")));

        c.removeImage("image2");

        assertEquals(List.of("image1"), c.getImageList());
        assertEquals(List.of("image1Path"), c.getImagePaths());
    }

    @Test
    void removeImageWithNullNameIsNoOp() throws Exception {
        StackingController c = new StackingController("Noise Stacking");

        Field imageNames = c.getClass().getDeclaredField("imageNamesList");
        Field imagePaths = c.getClass().getDeclaredField("imagePathsList");
        imageNames.setAccessible(true);
        imagePaths.setAccessible(true);

        imageNames.set(c, List.of("image1", "image2"));
        imagePaths.set(c, List.of("image1Path", "image2Path"));

        c.removeImage(null);

        assertEquals(List.of("image1", "image2"), c.getImageList());
        assertEquals(List.of("image1Path", "image2Path"), c.getImagePaths());
    }

    @Test
    void removeImageWithUnknownNameIsNoOp() throws Exception {
        StackingController c = new StackingController("Noise Stacking");

        Field imageNames = c.getClass().getDeclaredField("imageNamesList");
        Field imagePaths = c.getClass().getDeclaredField("imagePathsList");
        imageNames.setAccessible(true);
        imagePaths.setAccessible(true);

        imageNames.set(c, List.of("image1", "image2"));
        imagePaths.set(c, List.of("image1Path", "image2Path"));

        c.removeImage("x");

        assertEquals(List.of("image1", "image2"), c.getImageList());
        assertEquals(List.of("image1Path", "image2Path"), c.getImagePaths());
    }

    @Test
    void runStackingScriptThrowsWhenNoImages() throws Exception {
        StackingController c = new StackingController("Noise Stacking");

        assertThrows(MinimumImagesRequiredException.class, c::runStackingScript,
                "Con meno di 2 immagini deve essere lanciata MinimumImagesRequiredException");
    }

    @Test
    void runStackingScript_throwsWhenSingleImage() throws Exception {
        StackingController c = new StackingController("Noise Stacking");

        Field imageNames = c.getClass().getDeclaredField("imageNamesList");
        Field imagePaths = c.getClass().getDeclaredField("imagePathsList");
        imageNames.setAccessible(true);
        imagePaths.setAccessible(true);

        imageNames.set(c, List.of("image1"));
        imagePaths.set(c, List.of("image1Path"));

        assertThrows(MinimumImagesRequiredException.class, c::runStackingScript,
                "Con meno di 2 immagini deve essere lanciata MinimumImagesRequiredException");
    }
}