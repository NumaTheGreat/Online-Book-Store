package com.capstone.bookStore.Service;

import com.capstone.bookStore.Model.*;
import com.capstone.bookStore.Repository.CartRepository;
import com.capstone.bookStore.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;

    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalStateException("Cart with ID " + cartId + " not found."));
    }

    public void addBookToCart(Long cartId, Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        Cart cart = getCart(cartId);
        CartItem existingCartItem = findCartItem(cart, book.getId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setBook(book);
            newItem.setQuantity(1);
            newItem.setPrice(book.getPrice());
            cart.addItem(newItem);
        }

        cartRepository.save(cart);
    }

    public void removeBookFromCart(Long cartId, Long bookId) {
        Cart cart = getCart(cartId);
        cart.getItems().removeIf(item -> item.getBook().getId().equals(bookId));
        cartRepository.save(cart);
    }

    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public double getCartTotal(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private CartItem findCartItem(Cart cart, Long bookId) {
        return cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public void updateQuantity(Long cartId, Long bookId, int quantity) {
        // Fetch cart by ID
        Cart cart = getCart(cartId);

        // Find CartItem corresponding to bookId and update the quantity
        cart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(quantity),
                        () -> {
                            throw new IllegalStateException("Book with ID " + bookId + " not found in cart.");
                        }
                );
        cartRepository.save(cart);
    }

    public void checkout(Long cartId, Long userId) {
        Cart cart = getCart(cartId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        User user = userService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(getCartTotal(cartId));

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);

        // Save order to repository
        orderRepository.save(order);

        // Clear the cart after checkout
        clearCart(cartId);
    }

    public double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }


}