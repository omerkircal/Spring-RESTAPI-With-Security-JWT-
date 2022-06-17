package com.works.restcontrollers;

import com.works.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order")
public class OrderRestController {
    final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public ResponseEntity save(){
        return orderService.save();
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam long id){
        return orderService.delete(id);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return orderService.list();
    }

    @GetMapping("/getOrder")
    public ResponseEntity getOrder(long id){
        return orderService.getOrder(id);
    }

    @GetMapping("/customerOrder")
    public ResponseEntity customerOrder(){
        return orderService.customerOrder();
    }
}
