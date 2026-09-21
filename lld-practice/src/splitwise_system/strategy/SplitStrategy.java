package splitwise_system.strategy;

import splitwise_system.entities.Split;
import splitwise_system.entities.User;

import java.util.List;

public interface SplitStrategy {
    List<Split> calculateSplits(double totalAmount, User paidBy, List<User> participants, List<Double> splitValues);
}
