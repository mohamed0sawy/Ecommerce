package com.academy.Ecommerce.service;

import com.academy.Ecommerce.domainPrimitive.Quantity;
import com.academy.Ecommerce.model.Cart;
import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public List<CartItem> findCartItemByCartId(Long cartId){
        return cartItemRepository.findByCartId(cartId);
    }

    public Optional<CartItem> findCartItemByCartItemId(Long id){
        return cartItemRepository.findById(id);
    }

    public CartItem findCartItemByCartIdAndProductId(Long cartId, Long productId){
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    public CartItem saveCartItem(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItemById(Long id){
        cartItemRepository.deleteById(id);
    }

    public void deleteCartItemsByCartId(Long cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }

//    public List<CartItem> cleanCartItems(Long cartId) {
//        List<CartItem> cartItems = findCartItemByCartId(cartId);
//        Iterator<CartItem> iterator = cartItems.iterator();
//        while (iterator.hasNext()) {
//            CartItem cartItem = iterator.next();
//            Product product = cartItem.getProduct();
//            if (product == null) {
//                deleteCartItemById(cartItem.getId());
//                iterator.remove();
//            }
//        }
//        return cartItems;
//    }

    public void addCartItem(Cart cart, Product product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(new Quantity(quantity));
        saveCartItem(cartItem);
    }
    public void incrementQuantityByOne(Cart cart, Product product) {
        CartItem cartItem = findCartItemByCartIdAndProductId(cart.getId(), product.getId());
        cartItem.setQuantity(new Quantity(cartItem.getQuantity().value() + 1));
        cartItemRepository.save(cartItem);
    }
}
