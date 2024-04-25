import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Instock implements ProductStock {

    private LinkedHashMap<String, Product> products;

    public Instock() {
        this.products = new LinkedHashMap<>();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public boolean contains(Product product) {
        return contains(product.getLabel());
    }

    public boolean contains(String label) {
        return products.containsKey(label);
    }

    @Override
    public void add(Product product) {
        products.put(product.getLabel(), product);
    }

    @Override
    public void changeQuantity(String label, int quantity) {
        if (!contains(label)) {
            throw new IllegalArgumentException();
        }
        Product product = products.get(label);
        product.setQuantity(quantity);
    }

    @Override
    public Product find(int index) {
        return products.values()
                .stream().skip(index)
                .findFirst()
                .orElseThrow(IndexOutOfBoundsException::new);
    }

    @Override
    public Product findByLabel(String label) {
        Product product = products.get(label);
        if (product == null) {
            throw new IllegalArgumentException();
        }
        return product;
    }

    @Override
    public Iterable<Product> findFirstByAlphabeticalOrder(int count) {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findAllInRange(double lo, double hi) {
        return products.values()
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
        return products.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findFirstMostExpensiveProducts(int count) {
        if (products.size() < count){
            return new ArrayList<>();
        }

        return products.values()
                .stream().sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> findAllByQuantity(int quantity) {
        return findAllPredicate(product -> product.getQuantity() == quantity);
    }

    @Override
    public Iterator<Product> iterator() {
        return products.values().iterator();
    }
}
