package com.works.services;

import com.works.entities.Basket;
import com.works.entities.Customer;
import com.works.entities.Orders;
import com.works.entities.Product;
import com.works.repositories.BasketRepository;
import com.works.repositories.CustomerRepository;
import com.works.repositories.OrderRepository;
import com.works.repositories.ProductRepository;
import com.works.utils.REnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class OrderService {
    final OrderRepository orderRepository;
    final ProductService productService;
    final BasketRepository basketRepository;
    final CustomerRepository customerRepository;
    final ProductRepository productRepository;
    final HttpSession session;

    public OrderService(OrderRepository orderRepository, ProductService productService, BasketRepository basketRepository, CustomerRepository customerRepository, ProductRepository productRepository, HttpSession session) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.session = session;
    }

    public ResponseEntity<Map<REnum,Object>> save(){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        Orders orders=new Orders();
        int sum =0;

        Customer customer= (Customer) session.getAttribute("customer");
        if(customer!=null){
            List<Basket> basketList=basketRepository.findByCreatedByEqualsAndStatusFalse(customer.getEmail());
            if(basketList.size()>0){
                orders.setCustomer(customer);
                orders.setBaskets(basketList);
                for(Basket item:basketList){
                    sum = sum + item.getProduct().getProductPrice() * item.getQuantity();
                    Optional<Basket> optionalBasket=basketRepository.findById(item.getId());
                    optionalBasket.get().setStatus(true);
                    basketRepository.saveAndFlush(optionalBasket.get());
                }
                orders.setTotal(sum);
                orderRepository.saveAndFlush(orders);
                hm.put(REnum.status, true);
                hm.put(REnum.result, orders);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }else{
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Basket is empty");
                return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            hm.put(REnum.status, false);
            hm.put(REnum.message, "No such a customer ");
            return new ResponseEntity<>(hm, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<Map<REnum,Object>> delete(long id){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Optional<Orders> optionalOrders=orderRepository.findById(id);
            if(optionalOrders.isPresent()){
                orderRepository.deleteById(id);
                Orders orders=optionalOrders.get();
                List<Basket> basketList=orders.getBaskets();
                for(Basket basket:basketList){
                    Product product=basket.getProduct();
                    product.setProductQuaintity(product.getProductQuaintity()+ basket.getQuantity());
                    productRepository.saveAndFlush(product);
                }
                hm.put(REnum.status, true);
                return new ResponseEntity<>(hm, HttpStatus.OK);
            }else{
                hm.put(REnum.status, false);
                hm.put(REnum.message, "There is not such order id");
                return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            hm.put(REnum.status, false);
            hm.put(REnum.error, e.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<REnum,Object>> list(){
        Map<REnum, Object> hm = new HashMap<>();
        List<Orders> ordersList = orderRepository.findAll();
        hm.put(REnum.status, true);
        hm.put(REnum.result, ordersList);
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> getOrder(long id){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        List<Orders> ordersList=orderRepository.findByIdIs(id);
        hm.put(REnum.result,ordersList);
        return new ResponseEntity<>(hm,HttpStatus.OK);
    }

    public ResponseEntity<Map<REnum,Object>> customerOrder(){
        Map<REnum,Object> hm=new LinkedHashMap<>();
        try {
            Customer customer= (Customer) session.getAttribute("customer");
            List<Orders> ordersList=orderRepository.findByCustomer_Id(customer.getId());
            hm.put(REnum.status, true);
            hm.put(REnum.result, ordersList);
            return new ResponseEntity<>(hm, HttpStatus.OK);
        }catch (Exception e){
            hm.put(REnum.status, false);
            hm.put(REnum.error, e.getMessage());
            return new ResponseEntity<>(hm, HttpStatus.BAD_REQUEST);
        }
    }
}
