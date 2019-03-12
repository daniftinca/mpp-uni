import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SomeClassTest {

    @Test
    public void isPrim() {

        assertTrue(SomeClass.isPar(2));
        assertTrue(!SomeClass.isPar(3));
    }
}