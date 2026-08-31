package design_patterns.behavioral.command_pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

interface OrderCommand {
    void execute();
    void undo();
}

class Kitchen {
    public void prepareDish(String dish) {
        System.out.println("Preparing: " + dish);
    }

    public void cancelDish(String dish) {
        System.out.println("Cancelling: " + dish);
    }
}

class PlaceOrderCommand implements OrderCommand {
    private final Kitchen kitchen;
    private final String dish;

    public PlaceOrderCommand(Kitchen kitchen, String dish) {
        this.kitchen = kitchen;
        this.dish = dish;
    }

    @Override
    public void execute() {
         kitchen.prepareDish(dish);
    }

    @Override
    public void undo() {
         kitchen.cancelDish(dish);
    }
}

class CancelOrderCommand implements OrderCommand {
    private final Kitchen kitchen;
    private final String dish;

    public CancelOrderCommand(Kitchen kitchen, String dish) {
        this.kitchen = kitchen;
        this.dish = dish;
    }

    @Override
    public void execute() {
        kitchen.cancelDish(dish);
    }

    @Override
    public void undo() {
        kitchen.prepareDish(dish);
    }
}

class Waiter {
    private List<OrderCommand> pending = new ArrayList<>();
    private Stack<OrderCommand> history = new Stack<>();

    public void takeOrder(OrderCommand command) {
        pending.add(command);
    }

    public void submitOrders() {
        for(OrderCommand cmd: pending){
            cmd.execute();
            history.push(cmd);
        }
        pending.clear();
    }

    public void undoLast() {
        if (!history.isEmpty()){
            history.pop().undo();
        }
    }
}

public class RestaurantOrderSystem {
    public static void main(String[] args) {
         Kitchen kitchen = new Kitchen();
         Waiter waiter = new Waiter();
         waiter.takeOrder(new PlaceOrderCommand(kitchen, "Pasta"));
         waiter.takeOrder(new PlaceOrderCommand(kitchen, "Salad"));
         waiter.submitOrders();
         waiter.takeOrder(new CancelOrderCommand(kitchen, "Salad"));
         waiter.submitOrders();
         waiter.undoLast(); // undo the cancellation
    }
}
