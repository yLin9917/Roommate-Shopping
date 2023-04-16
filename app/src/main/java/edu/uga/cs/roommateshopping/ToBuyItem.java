package edu.uga.cs.roommateshopping;

/**
 * This class is use to define the to buy list
 */
public class ToBuyItem {
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
     * @param name
     * @param quantity
     */
    public ToBuyItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        isSelected = false;
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
