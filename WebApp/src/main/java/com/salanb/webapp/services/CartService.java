package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Cart;


import java.util.List;

public interface CartService {
    public Cart addCart(Cart ct);
    public Cart getCart(int c_id, int p_id);
    public List<Cart> getAllCarts();
    public Cart updateCart(Cart change);


}
