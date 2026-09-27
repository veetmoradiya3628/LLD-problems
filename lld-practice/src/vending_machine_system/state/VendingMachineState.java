package vending_machine_system.state;

import vending_machine_system.VendingMachine;
import vending_machine_system.enums.Coin;


public abstract class VendingMachineState {
    VendingMachine machine;

    VendingMachineState(VendingMachine machine) {
        this.machine = machine;
    }

    public abstract void insertCoin(Coin coin);
    public abstract void selectItem(String code);
    public abstract void dispense();
    public abstract void refund();
}
