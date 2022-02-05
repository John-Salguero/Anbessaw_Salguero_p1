package com.salanb.webapp.services;

import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.webapp.models.Cart;
import com.salanb.webapp.repositories.CartRepo;

import java.util.List;

public class CartServiceImpl implements CartService {

    private CartRepo ctr;

    public CartServiceImpl (CartRepo ctr) {
        this.ctr = ctr;
    }
    @Override
    public Cart addCart(Cart ct) {
        return ctr.addCart(ct);
    }

    @Override
    public Cart getCart(int c_id, int p_id) {
        return ctr.getCart(c_id, p_id);
    }

    @Override
    public List<Cart> getAllCarts() {
        return ctr.getAllCarts();
    }

    @Override
    public Cart updateCart(Cart change) {
        return ctr.updateCart(change);
    }


}
