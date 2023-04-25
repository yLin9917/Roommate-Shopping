package edu.uga.cs.roommateshopping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchasedItem implements Serializable {

    String name;
    double cost;
    List<ToBuyItem> items;
    int purchasedNum;


    /**
     * use to initialize the item
     * @param name item name
     * @param cost item cost
     * @param items purchased items
     * @param purchasedNum number of purchased
     */
    public PurchasedItem(String name, double cost, List<ToBuyItem> items, int purchasedNum) {
        this.name = name;
        this.cost = cost;
        this.items = items;
        this.purchasedNum = purchasedNum;
    }

    /**
     * return item name
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * return total cost
     * @return total cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * return list of items
     * @return list of items
     */
    public List<ToBuyItem> getItems() {
        return items;
    }

    /**
     * return number of purchased
     * @return
     */
    public int getPurchasedNum() {
        return purchasedNum;
    }
}
