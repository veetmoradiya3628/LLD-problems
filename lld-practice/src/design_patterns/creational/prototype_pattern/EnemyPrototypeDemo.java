package design_patterns.creational.prototype_pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface EnemyPrototype {
    EnemyPrototype clone();
}

class EnemyRegistry {
    private Map<String, Enemy> prototypes = new HashMap<>();

    public void register(String key, Enemy prototype) {
        prototypes.put(key, prototype);
    }

    public Enemy get(String key) {
        Enemy prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("No prototype registered for: " + key);
        }
        return prototype.clone();
    }
}


class Enemy implements EnemyPrototype {
    private String type;
    private int health;
    private double speed;
    private boolean armored;
    private String weapon;
    private List<String> inventory;

    public Enemy(String type, int health, double speed, boolean armored,
                 String weapon, List<String> inventory) {
        this.type = type;
        this.health = health;
        this.speed = speed;
        this.armored = armored;
        this.weapon = weapon;
        this.inventory = new ArrayList<>(inventory);
    }

    @Override
    public Enemy clone() {
        return new Enemy(type, health, speed, armored, weapon, new ArrayList<>(inventory));
    }

    public void setHealth(int health) { this.health = health; }
    public void addItem(String item) { inventory.add(item); }

    public void printStats() {
        System.out.println(type + " [Health: " + health +
                ", Speed: " + speed + ", Armored: " + armored +
                ", Weapon: " + weapon + ", Inventory: " + inventory + "]");
    }
}

public class EnemyPrototypeDemo {
    public static void main(String[] args) {
        EnemyRegistry registry = new EnemyRegistry();

        registry.register("flying", new Enemy("FlyingEnemy", 100, 12.0, false,
                "Laser", new ArrayList<>(List.of("Speed Boost"))));
        registry.register("armored", new Enemy("ArmoredEnemy", 300, 6.0, true,
                "Cannon", new ArrayList<>(List.of("Shield", "Helmet"))));

        Enemy e1 = registry.get("flying");
        Enemy e2 = registry.get("flying");
        e2.setHealth(80);
        e2.addItem("Smoke Bomb");

        Enemy e3 = registry.get("armored");

        e1.printStats();
        e2.printStats();
        e3.printStats();
    }
}
