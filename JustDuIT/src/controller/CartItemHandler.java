package controller;

import java.util.Vector;

import model.CartItem;

public class CartItemHandler {
	private static CartItemHandler instance;
	private Vector<CartItem> itemList;
	public CartItemHandler() {
		itemList = new Vector<CartItem>();
	}
	
	public static synchronized CartItemHandler getInstnace() {
		if(instance == null) {
			instance = new CartItemHandler();
		}
		return instance;
	}
	
	public Vector<CartItem> getListCartItem(){
		return this.itemList;
	}
	
	public CartItem addToCart(Integer productId, Integer quantity) {
		CartItem c = new CartItem();
		c.setProductId(productId);
		c.setQuantity(quantity);
		
		for(CartItem i : itemList) {
			if(i.getProductId() == productId) {
				i.setQuantity(quantity + i.getQuantity());
				return i;
			}
		}
		
		this.itemList.add(c);
		return c;
	}
	
	public void updateStock(Integer productId, Integer stock) {
		for(CartItem c : itemList) {
			if(c.getProductId() == productId) {
				c.setQuantity(stock);
			}
		}
	}
	
	public void deleteItem(Integer productId) {
		Integer target = -1;
		for(int i = 0; i<itemList.size(); i++) {
			CartItem c = itemList.get(i);
			if(c.getProductId() == productId) {
				target = i;
				break;
			}
		}
		if(target != -1) itemList.remove(target);
	}
	
	public void clearCartItemList() {
		this.itemList.clear();
	}
}
