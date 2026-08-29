package design_patterns.behavioral.observer_pattern;

import java.util.*;

interface AuctionObserver {
    void onNewBid(Auction auction);
}

class Auction {
    private String itemName;
    private double highestBid;
    private String highestBidder;
    private List<AuctionObserver> observers = new ArrayList<>();
    private boolean notifying = false;

    public Auction(String itemName) {
        this.itemName = itemName;
        this.highestBid = 0;
        this.highestBidder = "";
    }

    public void addObserver(AuctionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AuctionObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for(AuctionObserver observer: observers) {
            observer.onNewBid(this);
        }
    }

    public void placeBid(String bidderName, double amount) {
        if (amount > highestBid) {
            highestBid = amount;
            highestBidder = bidderName;
            notifyObservers();
        }
    }

    public String getItemName() { return itemName; }
    public double getHighestBid() { return highestBid; }
    public String getHighestBidder() { return highestBidder; }
}

class BidDisplay implements AuctionObserver {
    @Override
    public void onNewBid(Auction auction) {
        System.out.println("Bid Update -> [" + auction.getItemName() + "] Current bid: $" + String.format("%.2f", auction.getHighestBid()) + " by " + auction.getHighestBidder());
    }
}

class AutoBidder implements AuctionObserver {
    private String name;
    private double maxBid;

    public AutoBidder(String name, double maxBid) {
        this.name = name;
        this.maxBid = maxBid;
    }

    @Override
    public void onNewBid(Auction auction) {
        if (auction.getHighestBidder().equals(name)) return;
        double newBid = auction.getHighestBid() + 10;
        if (newBid <= maxBid) {
            System.out.println(name + " bids $" + String.format("%.2f", newBid) + " on " + auction.getItemName());
            auction.placeBid(name, newBid);
        }
    }
}

public class AuctionBidderDemo {
    public static void main(String[] args) {
         Auction auction = new Auction("Vintage Watch");
         BidDisplay display = new BidDisplay();
         AutoBidder bot = new AutoBidder("AutoBot", 150.0);
         auction.addObserver(display);
         auction.addObserver(bot);
         auction.placeBid("Alice", 50.0);
         auction.placeBid("Bob", 120.0);
         auction.placeBid("Alice", 200.0);
    }
}
