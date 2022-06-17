package com.works.restcontrollers;


import com.works.entities.Basket;
import com.works.services.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/basket")
public class BasketRestController {
    final BasketService basketService;

    public BasketRestController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Basket basket){
        return basketService.save(basket);
    }

    @GetMapping("/list")
    public ResponseEntity list(@RequestParam @Email(message = "E-mail Format Error") @NotBlank(message = "E mail can not blank") String email){
        return basketService.customerBasketList(email);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam Long id){
        return basketService.delete(id);
    }

    @PutMapping("/update")
    public ResponseEntity update(Long id,@NotNull(message = "Quantity can not be null") @Min(value = 1,message = "You must add a minimum of 1 pc") Integer quantity){
        return basketService.update(id,quantity);
    }
}
