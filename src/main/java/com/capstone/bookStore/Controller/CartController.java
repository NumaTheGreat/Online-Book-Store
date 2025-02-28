package com.capstone.bookStore.Controller;

import com.capstone.bookStore.Model.Book;
import com.capstone.bookStore.Model.Cart;
import com.capstone.bookStore.Service.BookService;
import com.capstone.bookStore.Service.CartService;
import com.capstone.bookStore.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    //DISPLAYS THE USER'S SHOPPING CART
    @GetMapping("/{cartId}")
    public String viewCart(@PathVariable Long cartId, Model model) {
        Cart cart = cartService.getCart(cartId);
        double cartTotal = cartService.getCartTotal(cartId);

        model.addAttribute("items", cart.getItems());
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("cartId", cartId);

        return "cart"; //
    }

    //ADDS A BOOK THE SHOPPING CART
    @PostMapping("/{cartId}/add/{bookId}")
    public String addBookToCart(@PathVariable Long cartId, @PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        cartService.addBookToCart(cartId, book);
        return "redirect:/cart/" + cartId;
    }

    //REMOVES A BOOK FROM THE SHOPPING CART
    @PostMapping("/{cartId}/remove/{bookId}")
    public String removeBookFromCart(@PathVariable Long cartId, @PathVariable Long bookId) {
        cartService.removeBookFromCart(cartId, bookId);
        return "redirect:/cart/" + cartId;
    }

    //UPDATES THE QUANTITY OF A BOOK IN THE SHOPPING CART
    @PostMapping("/{cartId}/updateQuantity/{bookId}")
    public String updateQuantity(@PathVariable Long cartId, @PathVariable Long bookId, @RequestParam int quantity) {
        cartService.updateQuantity(cartId, bookId, quantity);
        return "redirect:/cart/" + cartId;
    }

    //PROCESSES THE CHECK-OUT AND REDIRECTS TO ORDER HISTORY
    @PostMapping("/{cartId}/checkout")
    public String checkout(@PathVariable Long cartId,
                           @SessionAttribute("userId") Long userId,
                           RedirectAttributes redirectAttributes) {
        try {
            cartService.checkout(cartId, userId);
            return "redirect:/orders/history";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/" + cartId;
        }
    }

}