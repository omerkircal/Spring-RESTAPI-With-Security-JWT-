package com.works.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Product extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(message = "Product name cannot exceed 50 characters.", min = 0, max = 50)
    @NotBlank(message = "Please Enter Product Name")
    private String productName;
    @Length(message = "Product detail cannot exceed 500 characters.", min = 0, max = 500)
    @NotBlank(message = "Please Enter Product Detail")
    private String productDetail;
    @Digits(message = "The product price cannot be greater than 99.999 TL.", integer = 5, fraction = 2)
    @NotNull(message = "Please Enter Product Price")
    private Integer productPrice;
    private Integer productQuaintity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Category category;

    @OneToMany (mappedBy = "product",fetch =  FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Basket> baskets;
}
