package com.works.services;

import com.works.entities.Category;
import com.works.entities.Product;
import com.works.repositories.CategoryRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<Map<REnum,Object>> save(Product product){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        Optional<Category> optionalCategory=categoryRepository.findById(product.getCategory().getId());
        if(optionalCategory.isPresent()){
            Product p=productRepository.save(product);
            hm.put(REnum.status,true);
            hm.put(REnum.result,p);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }else{
            hm.put(REnum.status, false);
            hm.put(REnum.message, " There is not such a this Category Id");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        hm.put(REnum.result,productRepository.findAll());
        return new ResponseEntity<>(hm,HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> delete(long id){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            productRepository.deleteById(id);
            hm.put(REnum.result,true);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }catch (Exception e){
            hm.put(REnum.result,false);
            return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> update(Product product){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Optional<Product> optionalProduct=productRepository.findById(product.getId());
            Optional<Category> optionalCategory=categoryRepository.findById(product.getId());
            if(optionalProduct.isPresent()){
                productRepository.saveAndFlush(product);
                hm.put(REnum.status,true);
                hm.put(REnum.result,product);
                return new ResponseEntity<>(hm,HttpStatus.OK);
            }else if (!optionalCategory.isPresent()){
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not like an Category ID");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }else{
                hm.put(REnum.status,false);
                hm.put(REnum.message,"Insert Error");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            hm.put(REnum.status,false);
            hm.put(REnum.message,e.getMessage());
        }

        return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Map<REnum,Object>> searchProduct(String data) {
        Map<REnum,Object> hm=new LinkedHashMap<>();
        List<Product> productList =productRepository.findByProductNameContainsIgnoreCase(data);
        if(productList.size()>0){
            hm.put(REnum.status,true);
            hm.put(REnum.result,productList);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }else{
            hm.put(REnum.status,true);
            hm.put(REnum.message,"No Results Found");
            return new ResponseEntity<>(hm,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Map<REnum,Object>> searchProductCategory(long id) {
        Map<REnum,Object> hm=new LinkedHashMap<>();
        Optional<Category> optionalCategory=categoryRepository.findById(id);
        if(optionalCategory.isPresent()){
            List<Product> productList=productRepository.findByCategory_IdEquals(id);
            hm.put(REnum.status,true);
            hm.put(REnum.result,productList);
            return new ResponseEntity<>(hm,HttpStatus.OK);
        }else{
        hm.put(REnum.status,false);
        hm.put(REnum.message,"There is not like an Category ID");
        return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
        }
    }

}
