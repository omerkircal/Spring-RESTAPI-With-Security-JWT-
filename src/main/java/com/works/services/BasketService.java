package com.works.services;

import com.works.entities.Basket;
import com.works.entities.Customer;
import com.works.entities.Product;
import com.works.repositories.BasketRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.apache.catalina.LifecycleState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BasketService {
    final BasketRepository basketRepository;
    final ProductRepository productRepository;
    final HttpSession session;

    public BasketService(BasketRepository basketRepository, ProductRepository productRepository, HttpSession session) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.session = session;
    }

    public ResponseEntity<Map<REnum,Object>> save(Basket basket){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        Customer customer= (Customer) session.getAttribute("customer");
        List<Basket> basketList=basketRepository.findByCreatedByEqualsAndStatusFalse(customer.getEmail());

        boolean isSameProduct=false;
        long basketId=0;
        int oldBasketQuantity=0;

        Optional<Product> optionalProduct=productRepository.findById(basket.getProduct().getId());
        if(optionalProduct.isPresent()){
            for(Basket item:basketList){
                if(item.getProduct().getId()==basket.getProduct().getId()){
                    isSameProduct=true;
                    basketId=item.getId();
                    oldBasketQuantity= item.getQuantity();
                    break;
                }
            }
            Product product=optionalProduct.get();
            int stockQuantity= product.getProductQuaintity();
            int basketQuantity=basket.getQuantity();

            if(basketQuantity<=stockQuantity){
                product.setProductQuaintity(stockQuantity-basketQuantity);
                productRepository.saveAndFlush(product);
                basket.setProduct(product);
                if(isSameProduct){
                    basket.setId(basketId);
                    basket.setQuantity(basketQuantity+oldBasketQuantity);
                    basket.setCreatedBy(customer.getEmail());
                }
                basketRepository.saveAndFlush(basket);
                hm.put(REnum.status, true);
                hm.put(REnum.result, basket);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }else{
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Not enough stock");
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }
        }else{
            hm.put(REnum.status, false);
            hm.put(REnum.message, "There is not such a product");
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }
    }


    public ResponseEntity<Map<REnum,Object>> delete(long id){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Optional<Basket> optionalBasket=basketRepository.findById(id);
            if(optionalBasket.isPresent()){
                basketRepository.deleteById(id);
                Product product=optionalBasket.get().getProduct();
                product.setProductQuaintity(product.getProductQuaintity()+optionalBasket.get().getQuantity());
                productRepository.saveAndFlush(product);
                hm.put(REnum.result,true);
                return new ResponseEntity<>(hm,HttpStatus.OK);
            }else{
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not such a basket");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            hm.put(REnum.result,false);
            hm.put(REnum.message,e.getMessage());
            return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> update(Long id,Integer quantity){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Optional<Basket> optionalBasket=basketRepository.findById(id);
            if(optionalBasket.isPresent()){
                Basket oldBasket=optionalBasket.get();
                int oldBasketQuantity=oldBasket.getQuantity();
                oldBasket.setQuantity(quantity);
                basketRepository.saveAndFlush(oldBasket);
                Product product=oldBasket.getProduct();
                if(oldBasketQuantity>quantity){
                    product.setProductQuaintity(product.getProductQuaintity()+oldBasketQuantity-quantity);
                }else{
                    product.setProductQuaintity(product.getProductQuaintity()-oldBasketQuantity+quantity);
                }
                productRepository.saveAndFlush(product);
                hm.put(REnum.status, true);
                hm.put(REnum.message, "Update is successful");
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }else{
                hm.put(REnum.status,false);
                hm.put(REnum.message,"There is not such a basket");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            hm.put(REnum.status,false);
            hm.put(REnum.message,e.getMessage());
        }

        return new ResponseEntity<>(hm,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity customerBasketList(String email){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        List<Basket> basketList=basketRepository.findByCreatedByEqualsIgnoreCase(email);
        hm.put(REnum.status, true);
        hm.put(REnum.result, basketList);
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }
}
