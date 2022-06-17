package com.works.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Basket extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    Product product;

    boolean status=false;

    @NotNull
    @Range(message ="You must add a minimum of 1 pc" ,min = 1)
    private int quantity;
}
