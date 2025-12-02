package it.luigivannozzi.pixelstacker.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyTest {

    @Test
    void setStrategyNoise()  {
        StackingContext ctx = new StackingContext(List.of(), "Noise Stacking");
        assertNotNull(ctx.getStrategy());
        assertEquals(NoiseStrategy.class, ctx.getStrategy().getClass());
    }

    @Test void setStrategyFocus()  {
        StackingContext ctx = new StackingContext(List.of(), "Focus Stacking");
        assertNotNull(ctx.getStrategy());
        assertEquals(FocusStrategy.class, ctx.getStrategy().getClass());
    }
    @Test void setStrategyExposure()   {
        StackingContext ctx = new StackingContext(List.of(), "Exposure Stacking");
        assertNotNull(ctx.getStrategy());
        assertEquals(ExposureStrategy.class, ctx.getStrategy().getClass());
    }

    @Test
    void setStrategy_invalid_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> new StackingContext(List.of(), "abc"));
    }
}