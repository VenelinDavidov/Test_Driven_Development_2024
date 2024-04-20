package BubbleTest04;

import org.junit.Assert;
import org.junit.Test;
import p04_BubbleSortTest.Bubble;

public class BubbleTest04 {

    @Test
    public void testBubbleSort() {

        int[] numbers = {3, 5, 8, -5, -9, 10, 12};
        int[] sorted = {-9, -5, 3, 5, 8, 10, 12};

        Bubble.sort(numbers);
        Assert.assertArrayEquals(sorted, numbers);


    }
}
