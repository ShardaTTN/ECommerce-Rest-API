package com.tothenew.sharda.Dto.Response;

import com.tothenew.sharda.Model.Category;
import lombok.Data;
import java.util.List;

@Data
public class ViewCategoryDto {

    private Category currentCategory;
    private List<Category> childCategories;
}
