package edu.uga.cs.roommateshopping;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchasedItem implements Serializable {

    private String id;
    String name;
    double cost;
    List<ToBuyItem> itemNameList;

    /**
     * use to initialize the item
     * @param name item name
     * @param cost item cost
     * @param itemNameList purchased items
     */
    public PurchasedItem(String name, double cost, List<ToBuyItem> itemNameList) {
        this.name = name;
        this.cost = cost;
        this.itemNameList = itemNameList;
    }

    /**
     * get item id
     * @return item id
     */
    public String getId() {
        return id;
    }

    /**
     * set item id
     * @param id item id
     */
    public void setId(String id) {
        this.id = id;
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
        return itemNameList;
    }

    /**
     * to string method
     * @return string
     */
    @Override
    public String toString() {
        return "PurchasedItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", itemNameList=" + itemNameList +
                '}';
    }


}
