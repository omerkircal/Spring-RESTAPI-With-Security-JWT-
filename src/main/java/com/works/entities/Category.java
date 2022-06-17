package com.works.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class Category extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(message = "Category name cannot exceed 50 characters.", min = 0, max = 50)
    @NotBlank(message = "Please Enter Category Name")
    private String categoryName;
    private String categoryDetails;


}
