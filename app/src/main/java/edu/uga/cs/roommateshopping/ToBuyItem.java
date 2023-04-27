package edu.uga.cs.roommateshopping;

import java.io.Serializable;

/**
 * This class is use to define the to buy list
 */
public class ToBuyItem implements Serializable {
    private String name;
    private int quantity;
    private boolean isSelected;

    /**
     * constructor
     */
    public ToBuyItem() {
        this.name = "";
        this.quantity = 0;
        isSelected = false;
    }

    /**
     * use to initialize the item
     * @param name item name
     */
    public ToBuyItem(String name) {
        this.name = name;
        this.quantity = 0;
        this.isSelected = false;
    }

//    /**
//     * use to initialize the item
//     * @param name item name
//     * @param quantity item quantity
//     */
//    public ToBuyItem(String name, int quantity) {
//        this.name = name;
//        this.quantity = quantity;
//        isSelected = false;
//    }

    public ToBuyItem(String name, int quantity, Boolean selected) {
        this.name = name;
        this.quantity = quantity;
        this.isSelected = selected;
    }

    /**
     * get quantity
     *
     * @return quantity
     */
    public String getQuantity() {
        return String.valueOf(quantity);
    }

    /**
     * get name
     * @return name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * return isSelected
     * @return isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * set isSelected
     * @param selected isSelected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "ToBuyItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
