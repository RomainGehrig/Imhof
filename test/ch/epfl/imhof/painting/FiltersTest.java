package ch.epfl.imhof.painting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.HashMap;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import static ch.epfl.imhof.painting.Filters.tagged;

public class FiltersTest {

    private Attributes newSampleAttributes() {
        HashMap<String, String> testData = new HashMap<>();
        testData.put("testKey 1", "testValue 1");
        testData.put("testKey 2", "testValue 2");
        testData.put("testKey 3", "testValue 3");
        testData.put("testKey 4", "23");
        return new Attributes(testData);
    }

    @Test
    public void taggedWorks() {
        assertTrue(tagged("testKey 1").test(new Attributed<Integer>(5,newSampleAttributes())));
        assertFalse(tagged("testKey 5").test(new Attributed<Integer>(5,newSampleAttributes())));
    }
}
