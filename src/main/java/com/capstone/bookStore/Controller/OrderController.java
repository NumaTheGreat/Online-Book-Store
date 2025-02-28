    package com.capstone.bookStore.Controller;

    import com.capstone.bookStore.Service.OrderService;
    import com.capstone.bookStore.Model.Order;
    import jakarta.servlet.http.HttpSession;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    import java.time.format.DateTimeFormatter;
    import java.util.List;


    @Controller
    @RequestMapping("/orders")
    public class OrderController {

        @Autowired
        private OrderService orderService;


        @PostMapping("/{cartId}/checkout")
        public String checkout(@PathVariable Long cartId, @SessionAttribute("userId") Long userId, RedirectAttributes redirectAttributes) {
            try {
                orderService.checkout(cartId, userId);
                return "redirect:/orders/history";
            } catch (IllegalStateException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/cart/" + cartId;
            }
        }



        @GetMapping("/history")
        public String viewOrderHistory(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
            Long userId = (Long) session.getAttribute("userId");

            if (userId == null) {
                redirectAttributes.addFlashAttribute("error", "You must log in to view your order history.");
                return "redirect:/users/login";
            }

            List<Order> orders = orderService.getOrderHistory(userId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            orders.forEach(order -> {
                if (order.getOrderDate() != null) {
                    order.setFormattedOrderDate(order.getOrderDate().format(formatter));
                }
            });

            model.addAttribute("orders", orders);
            return "orderHistory";
        }



    }
