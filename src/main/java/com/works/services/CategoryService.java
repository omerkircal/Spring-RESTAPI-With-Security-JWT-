package com.works.services;

import com.works.entities.Category;
import com.works.repositories.CategoryRepository;
import com.works.utils.REnum;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {
    final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Category category){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        Optional<Category> optionalCategory=categoryRepository.findByCategoryNameEqualsIgnoreCase(category.getCategoryName());
        if(!optionalCategory.isPresent()){
            Category c=categoryRepository.save(category);
            hm.put(REnum.status,true);
            hm.put(REnum.result,c);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }else{
            hm.put(REnum.status,false);
            hm.put(REnum.result,"There is already a category with this name");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }
    }


    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        hm.put(REnum.result,categoryRepository.findAll());
        return new ResponseEntity<>(hm,HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> delete(long id){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            categoryRepository.deleteById(id);
            hm.put(REnum.result,true);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }catch (Exception e){
            hm.put(REnum.result,false);
            return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> update(Category category){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Optional<Category> c=categoryRepository.findById(category.getId());
            if(c.isPresent()){
                categoryRepository.saveAndFlush(category);
                hm.put(REnum.status,true);
                hm.put(REnum.result,category);
                return new ResponseEntity<>(hm,HttpStatus.OK);
            }else{
                hm.put(REnum.status,false);
                hm.put(REnum.message,"Insert Error");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            hm.put(REnum.status,false);
            hm.put(REnum.message,e.getMessage());
        }
        return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
    }
}
