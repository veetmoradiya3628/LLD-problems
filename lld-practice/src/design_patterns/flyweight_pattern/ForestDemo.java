package design_patterns.flyweight_pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Flyweight interface
interface TreeType {
    void render(int x, int y);
}

// Concrete flyweight
class ConcreteTreeType implements TreeType {
    private final String name;
    private final String color;
    private final String texture;

    public ConcreteTreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    @Override
    public void render(int x, int y) {
        System.out.println("Rendering " + name + " tree [color=" + color +
                ", texture=" + texture + "] at (" + x + "," + y + ")");
    }
}

// Flyweight factory
class TreeTypeFactory {
    private final Map<String, TreeType> treeTypes = new HashMap<>();

    public TreeType getTreeType(String name, String color, String texture) {
        String key = name + "_" + color + "_" + texture;
        treeTypes.putIfAbsent(key, new ConcreteTreeType(name, color, texture));
        return treeTypes.get(key);
    }

    public int getTypeCount() {
        return treeTypes.size();
    }
}

// Extrinsic state holder
class Tree {
    private final TreeType type;
    private final int x;
    private final int y;

    public Tree(TreeType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        type.render(x, y);
    }
}

// Client
class Forest {
    private final TreeTypeFactory factory = new TreeTypeFactory();
    private final List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = factory.getTreeType(name, color, texture);
        trees.add(new Tree(type, x, y));
    }

    public void render() {
        for (Tree tree : trees) {
            tree.draw();
        }
        System.out.println("\nTotal trees planted: " + trees.size());
        System.out.println("Unique tree types created: " + factory.getTypeCount());
    }
}

public class ForestDemo {
    public static void main(String[] args) {
        Forest forest = new Forest();

        forest.plantTree(10, 20, "Oak", "dark green", "rough bark");
        forest.plantTree(50, 80, "Pine", "green", "needle texture");
        forest.plantTree(30, 60, "Oak", "dark green", "rough bark");
        forest.plantTree(70, 40, "Birch", "light green", "white bark");
        forest.plantTree(90, 10, "Pine", "green", "needle texture");

        forest.render();
    }
}
