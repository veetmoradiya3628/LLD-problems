package design_patterns.behavioral.iterator_pattern;

import java.util.*;

class Product {
    private int id;
    private String name;
    private double price;

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

class ProductCatalog {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getPage(int offset, int limit) {
        int end = Math.min(offset + limit, products.size());
        if (offset >= products.size()) return Collections.emptyList();
        return products.subList(offset, end);
    }

    public int getTotalCount() { return products.size(); }

    public Iterator<Product> createPaginatedIterator(int pageSize) {
        return new PaginatedIterator(this, pageSize);
    }
}

class PaginatedIterator implements Iterator<Product> {
    private ProductCatalog catalog;
    private int pageSize;
    private List<Product> currentPage;
    private int pageIndex;
    private int globalOffset;
    private int pageNumber;

    public PaginatedIterator(ProductCatalog catalog, int pageSize) {
        this.catalog = catalog;
        this.pageSize = pageSize;
        this.pageIndex = 0;
        this.globalOffset = 0;
        this.pageNumber = 0;
        loadNextPage();
    }

    @Override
    public boolean hasNext() {
        if (pageIndex < currentPage.size()) return true;
        if (globalOffset < catalog.getTotalCount()) {
            loadNextPage();
            return true;
        }
        return false;
    }

    @Override
    public Product next() {
        return currentPage.get(pageIndex++);
    }

    private void loadNextPage() {
        currentPage = catalog.getPage(globalOffset, pageSize);
        pageNumber++;
        System.out.println("Loading page " + pageNumber + "...");
        globalOffset += currentPage.size();
        pageIndex = 0;
    }
}

public class PaginatedIteratorDemo {
    public static void main(String[] args) {
         ProductCatalog catalog = new ProductCatalog();
         catalog.addProduct(new Product(1, "Laptop", 999.99));
         catalog.addProduct(new Product(2, "Mouse", 29.99));
         catalog.addProduct(new Product(3, "Keyboard", 79.99));
         catalog.addProduct(new Product(4, "Monitor", 349.99));
         catalog.addProduct(new Product(5, "Headphones", 149.99));
         catalog.addProduct(new Product(6, "USB Cable", 9.99));
         catalog.addProduct(new Product(7, "Mouse Pad", 19.99));
         catalog.addProduct(new Product(8, "Desk Lamp", 44.99));
         catalog.addProduct(new Product(9, "Speakers", 89.99));
         catalog.addProduct(new Product(10, "Webcam", 69.99));

         Iterator<Product> iterator = catalog.createPaginatedIterator(3);
         while (iterator.hasNext()) {
             System.out.println("  " + iterator.next());
         }
    }
}
