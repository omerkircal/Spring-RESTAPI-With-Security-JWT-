package com.works.restcontrollers;

import com.works.entities.Category;
import com.works.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryRestController {
    final CategoryService categoryService;

    CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public ResponseEntity save(@Valid @RequestBody Category category){
        return categoryService.save(category);
    }

    @GetMapping("/list")
    public ResponseEntity list(){
        return categoryService.list();
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestParam long id){
        return categoryService.delete(id);
    }

    @PutMapping("/update")
    public ResponseEntity update(@Valid @RequestBody Category category){
        return categoryService.update(category);
    }
}
