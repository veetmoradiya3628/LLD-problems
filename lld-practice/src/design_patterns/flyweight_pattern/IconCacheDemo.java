package design_patterns.flyweight_pattern;

import java.util.LinkedHashMap;
import java.util.Map;

interface PageIcon {
    void display(String url, int x, int y);
}

class ConcretePageIcon implements PageIcon {
    private final String iconType;
    private final String color;
    private final int size;

    public ConcretePageIcon(String iconType, String color, int size) {
        this.iconType = iconType;
        this.color = color;
        this.size = size;
    }

    @Override
    public void display(String url, int x, int y) {
        System.out.println("[" + iconType + ", " + color + ", " + size + "px] at url (" + x + "," + y + ")");
    }
}

class IconCacheFactory {
    private LinkedHashMap<String, PageIcon> cache;
    private int maxCapacity;

    public IconCacheFactory(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.cache = new LinkedHashMap<>(16, 0.75f, true);
    }

    public PageIcon getIcon(String iconType, String color, int size) {
        String key = iconType + "_" + color + "_" + size;
        if (cache.containsKey(key)) {
            System.out.println("Cache HIT: " + key);
            return cache.get(key);
        }
        if (cache.size() >= maxCapacity) {
            String lruKey = cache.keySet().iterator().next();
            cache.remove(lruKey);
            System.out.println("Evicting icon: " + lruKey);
        }
        System.out.println("Cache MISS: " + key);
        PageIcon icon = new ConcretePageIcon(iconType, color, size);
        cache.put(key, icon);
        return icon;
    }

    public int getCacheSize() {
        return cache.size();
    }
}

public class IconCacheDemo {
    public static void main(String[] args) {
         IconCacheFactory cache = new IconCacheFactory(3);

         cache.getIcon("favicon", "blue", 16).display("google.com", 10, 10);
         cache.getIcon("bookmark", "gold", 24).display("github.com", 30, 10);
         cache.getIcon("history", "gray", 16).display("stackoverflow.com", 50, 10);
         cache.getIcon("favicon", "blue", 16).display("google.com", 70, 10);  // HIT
         cache.getIcon("download", "green", 32).display("example.com", 90, 10);  // evicts LRU

         System.out.println("Cache size: " + cache.getCacheSize());
    }
}
