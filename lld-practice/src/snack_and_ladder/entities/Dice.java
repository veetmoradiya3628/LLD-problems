package snack_and_ladder.entities;

public class Dice {
    private final int minValue;
    private final int maxValue;

    public Dice(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

//    private static final int[] SEQUENCE = { 3, 4, 6, 6, 5, 4, 1, 1, 2, 2, 2, 5 };
//    private static int sequenceIndex = 0;
//
//    public int roll() {
//        int value = SEQUENCE[sequenceIndex % SEQUENCE.length];
//        sequenceIndex++;
//        return value;
//    }

    public int roll() {
        return (int) (Math.random() * (maxValue - minValue + 1) + minValue);
    }
}
