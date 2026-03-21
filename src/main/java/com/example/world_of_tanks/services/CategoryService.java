package com.example.world_of_tanks.services;

import com.example.world_of_tanks.models.Category;
import com.example.world_of_tanks.models.enums.CategoryEnum;
import com.example.world_of_tanks.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void seedCategories() {

        if (categoryRepository.count() != 0) {
            return;
        }

        for (CategoryEnum categoryName : CategoryEnum.values()) {

            String description = switch (categoryName) {
                case LIGHT_TANK -> "Fast and agile scout vehicles with low armor but high mobility. Ideal for reconnaissance and flanking maneuvers.";
                case MEDIUM_TANK -> "Versatile all-rounders balancing firepower, armor, and speed. The backbone of any armored division.";
                case HEAVY_TANK -> "Heavily armored frontline fighters with devastating firepower. Built to break through enemy defenses.";
            };

            Category category = new Category().setName(categoryName).setDescription(description);

            this.categoryRepository.save(category);
        }

    }
}

