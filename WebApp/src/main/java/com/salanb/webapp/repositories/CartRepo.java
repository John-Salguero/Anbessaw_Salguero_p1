package com.salanb.webapp.repositories;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Cart;

import java.util.List;

public interface CartRepo {
    public Cart addCart(Cart ct);
    public Cart getCart(int p_id, int c_id);
    public List<Cart> getAllCarts();
    public Cart updateCart(Cart change);

}
