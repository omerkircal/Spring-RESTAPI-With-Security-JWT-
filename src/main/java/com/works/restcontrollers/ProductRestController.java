package com.works.restcontrollers;

import com.works.entities.Product;
import com.works.services.ProductService;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/product")
public class ProductRestController {
    final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Product product){
        return productService.save(product);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return productService.list();
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam long id){
        return productService.delete(id);
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Product product){
        return productService.update(product);
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam @Length(message = "You must enter a minimum of 3 characters to search.",min = 3) String data){
        return productService.searchProduct(data);
    }

    @GetMapping("/searchByCategory")
    public ResponseEntity searchByCategory(@RequestParam long id){
        return productService.searchProductCategory(id);
    }
}
