package CustomLinkedList05;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import p05_CustomLinkedList.CustomLinkedList;

public class TestCustomLinkedList05 {

    CustomLinkedList<String> list;

    @Before
    public void setUp() {
        list = new CustomLinkedList<>();
        list.add("Pesho");
        list.add("Tosho");
        list.add("Gosho");
    }

    @Test
    public void testAddShouldAdd() {
        int previousSize = list.getCount();
        list.add("Andrei");
        int currentSize = list.getCount();
        Assert.assertEquals(previousSize + 1, currentSize);
        Assert.assertEquals(list.getCount() - 1, list.indexOf("Andrei"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetShouldThrowWithNegativeIndex() {
        list.get(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetShouldThrowWithBigIndex() {
        list.get(list.getCount() + 1);
    }

    @Test
    public void testGetShouldGet() {
        Assert.assertEquals("Tosho", list.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetShouldThrowWithBigIndex() {
        list.set(list.getCount() + 1, "Andrei");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetShouldThrowWithNegativeIndex() {
        list.set(-2, "Tosho");
    }

    @Test
    public void testSetShouldSet() {
        list.set(1, "Ivan");
        Assert.assertEquals("Ivan", list.get(1));
    }

    @Test
    public void testIndexOfShouldFindIndex() {
        Assert.assertEquals(1, list.indexOf("Tosho"));

    }

    @Test
    public void testIndexOfShouldNotFindIndex() {
        Assert.assertEquals(-1, list.indexOf("Venelin"));
    }

    @Test
    public void testContainsShouldReturnTrue() {
        Assert.assertTrue(list.contains("Gosho"));
    }

    @Test
    public void testContainsShouldReturnFalse() {
        Assert.assertFalse(list.contains("Dimitar"));
    }

    @Test
    public void testRemoveWithMissingElement() {
        Assert.assertEquals(-1, list.remove("Anatoli"));
    }

    @Test
    public void testRemoveShouldRemoveElement() {
        int countBeforeRemove = list.getCount();
        Assert.assertEquals(1, list.remove("Tosho"));
        int countAfterRemove = list.getCount();
        Assert.assertEquals(countBeforeRemove - 1, countAfterRemove);
        Assert.assertEquals(-1, list.indexOf("Tosho"));
    }

    @Test
    public void testRemoveAtIndexShouldRemove() {
        int countBeforeRemove = list.getCount();
        Assert.assertEquals("Tosho", list.removeAt(1));
        int countAfterRemove = list.getCount();
        Assert.assertEquals(countBeforeRemove - 1, countAfterRemove);

    }
}
