package com.tothenew.sharda.Model;

import com.tothenew.sharda.Service.ListToStringConverter;
import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class CategoryMetadataFieldValues {

    @SequenceGenerator(name = "category_metadata_field_values_sequence", sequenceName = "category_metadata_field_values_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "category_metadata_field_values_sequence")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_metadata_field_id")
    CategoryMetadataField categoryMetadataField;

    @Convert(converter = ListToStringConverter.class)
    private Set<String> valueList;
}