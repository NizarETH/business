package com.euphor.paperpad.Beans;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class CartItem extends RealmObject {


    @PrimaryKey
	private int id;

    private RealmList<CartItem> cartItems=new RealmList<>();
	private int price_id;
	private Child_pages child_page;
	private CartItem parentItem;
	private int order;
	private int quantity;
	private Formule formule;
	private String price;
	private String price_label;
	private String name;
	private int duplicate ;



    public RealmList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(RealmList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getPrice_id() {
        return price_id;
    }

    public void setPrice_id(int price_id) {
        this.price_id = price_id;
    }

    public Child_pages getChild_page() {
        return child_page;
    }

    public void setChild_page(Child_pages child_page) {
        this.child_page = child_page;
    }

    public CartItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(CartItem parentItem) {
        this.parentItem = parentItem;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        /*id= Increment.Primary_Cart(quantity);*/
        this.quantity = quantity;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_label() {
        return price_label;
    }

    public void setPrice_label(String price_label) {
        this.price_label = price_label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(int duplicate) {
        this.duplicate = duplicate;
    }

    public CartItem() {
    }

    public CartItem(int id, int price_id, Child_pages child_page, CartItem parentItem, int order, int quantity, Formule formule, String price, String price_label, String name, int duplicate) {
        this.id = id;
        this.price_id = price_id;
        this.child_page = child_page;
        this.parentItem = parentItem;
        this.order = order;
        this.quantity = quantity;
        this.formule = formule;
        this.price = price;
        this.price_label = price_label;
        this.name = name;
        this.duplicate = duplicate;
    }
    public CartItem(int price_id, Child_pages child_page,
                    CartItem parentItem, int order, int quantity, Formule formule,
                    String price, String name, RealmList<CartItem> cartItems) {
        super();
        this.price_id = price_id;
        this.child_page = child_page;
        this.parentItem = parentItem;
        this.order = order;
        this.quantity = quantity;
        this.formule = formule;
        this.price = price;
        this.name = name;
        this.cartItems = cartItems;
    }

    public CartItem(int id,int price_id, Child_pages child_page,
                    CartItem parentItem, int order, int quantity, Formule formule,
                    String price, String name, String price_label, RealmList<CartItem> cartItems) {
        super();
        this.id = id;
        this.price_id = price_id;
        this.child_page = child_page;
        this.parentItem = parentItem;
        this.order = order;
        this.quantity = quantity;
        this.formule = formule;
        this.price = price;
        this.name = name;
        this.price_label = price_label;
        this.cartItems = cartItems;
    }
}
