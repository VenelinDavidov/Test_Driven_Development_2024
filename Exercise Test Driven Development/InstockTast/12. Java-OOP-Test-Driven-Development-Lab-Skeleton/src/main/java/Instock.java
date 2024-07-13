import jdk.jshell.spi.ExecutionControl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Instock implements ProductStock {

    Map<String, Product> productMap;

    public Instock() {
        this.productMap = new LinkedHashMap<>();
    }

    @Override
    public int getCount() {
        return productMap.size();
    }

    @Override
    public boolean contains(Product product) {
        return contains(product.getLabel());
    }

    public boolean contains(String label) {
        return productMap.containsKey(label);
    }

    @Override
    public void add(Product product) {
        productMap.put(product.getLabel(), product);
    }

    @Override
    public void changeQuantity(String product, int quantity) {
        if (!contains(product)) {
            throw new IllegalArgumentException();
        }
        Product product1 = productMap.get(product);
        product1.setQuantity(quantity);
    }

    @Override
    public Product find(int index) {
        return productMap.values()
                .stream()
                .skip(index)
                .findFirst()
                .orElseThrow(IndexOutOfBoundsException::new);
    }

    @Override
    public Product findByLabel(String label) {

        Product product = productMap.get(label);
        if (product == null) {
            throw new IllegalArgumentException();
        }
        return product;
    }

    @Override
    public Iterable<Product> findFirstByAlphabeticalOrder(int count) {

        return productMap.values()
                .stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findAllInRange(double lo, double hi) {

        return productMap.values()
                .stream()
                .filter(product -> product.getPrice() > lo && product.getPrice() <= hi)
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findAllByPrice(double price) {
        return findAllPredicate(product -> product.getPrice() == price);

    }

    private Iterable<Product> findAllPredicate(Predicate<Product> predicate) {
        return productMap.values()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findFirstMostExpensiveProducts(int count) {

        if (productMap.size() <count){
            throw new IllegalArgumentException();
        }
        return productMap.values()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findAllByQuantity(int quantity) {
        return findAllPredicate(product -> product.getQuantity() == quantity);
    }

    @Override
    public Iterator<Product> iterator() {
        return productMap.values().iterator();
    }

}
