import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.*;

public class InstockTest {

    private Instock instock;    //в наличност

    List<Product> productList; // лист от продукти

    private static final double BEGIN = 10;
    private static final double END = 100;
    private static final double DUPLICAT_PRICE = 50;

    @Before
    public void setUp() {
        this.instock = new Instock(); // празен Instock
        this.productList = new ArrayList<>();  // празен лист
        this.prepareProduct();
    }

    public void prepareProduct() {
        Product product1 = new Product("Kamenitsa", 1.5, 10);
        Product product2 = new Product("Ariana", 1.3, 12);
        Product product3 = new Product("Zagorka", 1.6, 11);
        Product product4 = new Product("Beks", 1.8, 19);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
    }

    public List<Product> addProduct() {
        productList.forEach(product -> instock.add(product));
        return productList;
    }

    @Test
    public void test_Contains_Returns_Correct_Result() {
        instock.add(productList.get(0));
        assertTrue(instock.contains(productList.get(0)));
        assertFalse(instock.contains(new Product("not_added", 15, 100)));
    }

    @Test
    public void test_Count_Returns_Correct_Result() {
        assertEquals(0, instock.getCount());
        instock.add(productList.get(0));
        assertEquals(1, instock.getCount());
    }

    @Test
    public void test_changeQuantity_Should_Given_Amount() {
        instock.add(productList.get(0));
        int expected = productList.get(0).getQuantity() * 7;
        String actual = productList.get(0).getLabel();
        instock.changeQuantity(actual, expected);
        assertEquals(expected, productList.get(0).getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_changeQuantity_Should_Return_NoSuch_Stock() {
        instock.changeQuantity(productList.get(0).getLabel(), 1520);
    }

    @Test
    public void test_FindByIndex_Should_Return_ProductInsertionOreder() {
        List<Product> products = addProduct();
        int index = 3;
        String expectedLabel = products.get(index).getLabel();
        Product product = instock.find(index);
        assertNotNull(product);
        String actualLabel = product.getLabel();
        assertEquals(expectedLabel, actualLabel);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void test_FindByIndex_Should_Throw_IndexOutOfBounds() {
        instock.find(instock.getCount() + 1);
    }

    @Test
    public void test_findByLabel_Should_Return_Label() {
        instock.add(productList.get(0));
        Product actualFindlabel = instock.findByLabel(productList.get(0).getLabel());
        String expectedLabel = productList.get(0).getLabel();
        assertNotNull(actualFindlabel);
        assertEquals(expectedLabel, actualFindlabel.getLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_findByLabel_Should_Throw_Exception() {
        instock.findByLabel(productList.get(0).getLabel());
    }

    @Test
    public void test_findFirstByAlphabeticalOrder_Should_Return_ProductsOrderedByLabel() {
        List<Product> expectedProducts = addProduct().stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .collect(Collectors.toList());

        List<Product> actualProducts = convertIterableToList(instock.findFirstByAlphabeticalOrder(expectedProducts.size()));

        //List<Product> actualProducts = toList(instock.findFirstByAlphabeticalOrder(expectedProducts.size()));

        assertProductsListEqual(expectedProducts, actualProducts);
    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnCorrectCountProducts() {
        int count = addProduct().size() - 2;
        List<Product> products = convertIterableToList(instock.findFirstByAlphabeticalOrder(count));
        assertEquals(count, products.size());
    }

    //Methods convert Iterable to List A)

//    private List<Product> toList (Iterable<Product> iterable) {
//        assertNotNull(iterable);
//        return StreamSupport.stream(iterable.spliterator(), false)
//                .collect(Collectors.toList());
//    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnEmptySetWhenCountsIsTooLarge() {
        List<Product> products = convertIterableToList(instock.findFirstByAlphabeticalOrder(1));
        assertTrue(products.isEmpty());

    }

    @Test
    public void test_findAllInRange_Should_ReturnCorrectRange() {
        addProduct();

        List<Product> products = convertIterableToList(instock.findAllInRange(BEGIN, END));
        products.stream()
                .mapToDouble(Product::getPrice)
                .forEach(p -> assertTrue(p > BEGIN && p <= END));
    }

    @Test
    public void test_FindAllInPriceRangeShouldReturnInDescendingOrder() {
        List<Product> expected = addProduct().stream()
                .filter(p -> p.getPrice() > BEGIN && p.getPrice() <= END)
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());

        List<Product> actual = convertIterableToList(instock.findAllInRange(BEGIN, END));

        assertProductsListEqual(expected, actual);
    }

    @Test
    public void test_FindAllInPriceRangeShouldReturnEmptyCollections() {
        double max = addProduct().stream()
                .mapToDouble(Product::getPrice)
                .max()
                .orElse(0.00);

        List<Product> products = convertIterableToList(instock.findAllInRange(max, max + 1));
        assertTrue(products.isEmpty());

    }

    @Test
    public void test_findAllByPrice_Should_ReturnPrice() {
        addProduct();
        List<Product> products = convertIterableToList(instock.findAllByPrice(DUPLICAT_PRICE));
        products.forEach(product -> assertEquals(DUPLICAT_PRICE, product.getPrice(), 0.00));
    }

    @Test
    public void test_findAllByPrice_Should_ReturnNoMatchingPrice() {
        addProduct();
        List<Product> products = convertIterableToList(instock.findAllByPrice(1000));
        assertTrue(products.isEmpty());
    }

    @Test
    public void test_findFirstMostExpensiveProducts_ShouldReturnCorrectCountOfProducts() {
        addProduct();
        int count = 3;
        List<Product> products = convertIterableToList(instock.findFirstMostExpensiveProducts(count));
        assertEquals(count, products.size());
    }

    @Test
    public void testFindFirstMostExpensiveProductsShouldReturnMostExpensiveProduct() {
        addProduct();
        int count = 3;
        List<Product> actual = convertIterableToList(instock.findFirstMostExpensiveProducts(count));

        List<Product> expected = addProduct().stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(count)
                .collect(Collectors.toList());

        assertEquals(expected.size(), actual.size());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMostExpensiveProductsEmptySetIfCountIsLargerThanSize() {
        int size = addProduct().size();
        List<Product> products = convertIterableToList(instock.findFirstMostExpensiveProducts(size + 1));
        assertTrue(products.isEmpty());
    }


    @Test
    public void testFindAllByQuantityShouldReturnEmptySetWhenNoMatches() {
        addProduct();
        List<Product> products = convertIterableToList(instock.findAllByQuantity(1000));
        assertTrue(products.isEmpty());
    }

    @Test
    public void testFindAllByQuantityShouldReturnMatchingQuantityOnly() {
        addProduct();
        List<Product> products = convertIterableToList(instock.findAllByQuantity(productList.get(0).getQuantity()));
        products.forEach(product -> assertEquals(productList.get(0).getQuantity(), product.getQuantity()));
    }

    @Test
    public void testIterableAllProduct (){

        List<Product> expected = addProduct();
        Iterator<Product> iterator = instock.iterator();
        assertNotNull(iterator);
        Iterable<Product> iterable = ()-> iterator;
        List<Product> actual = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
        assertProductsListEqual(expected,actual);
    }






    //Methods convert Iterable to List A)
    private List<Product> convertIterableToList(Iterable<Product> iterable) {

        assertNotNull(iterable);
        List<Product> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    //Methods
    private static void assertProductsListEqual(List<Product> expectedProducts, List<Product> actualProducts) {

        assertEquals(expectedProducts.size(), actualProducts.size());
        for (int i = 0; i < expectedProducts.size(); i++) {

            String expectedLabel = expectedProducts.get(i).getLabel();
            String actualLabel = actualProducts.get(i).getLabel();
            assertEquals(expectedLabel, actualLabel);
        }
    }
}




