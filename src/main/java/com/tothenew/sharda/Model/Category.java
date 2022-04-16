package com.tothenew.sharda.Model;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Category {

    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "category_sequence")
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id")
    private Category category;

}