package design_patterns.creational.singleton_pattern;

public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    // not thread safe
    public static LazySingleton getInstance(){
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
