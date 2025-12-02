package it.luigivannozzi.pixelstacker.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    @BeforeEach
    void resetRouterSingleton() throws Exception {
        Field f = Router.class.getDeclaredField("instance");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Test
    void getInstanceBeforeInit() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, Router::getInstance);
        assertTrue(ex.getMessage().contains("non inizializzato"));
    }
}