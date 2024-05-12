package com.jjs.ClothingInventorySaleReformPlatform.domain.product.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.Order;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.dto.response.SaveWaterDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Category;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;



    @Transactional
    public SaveWaterDTO calculateWaterUsage() {
        List<Category> categories = categoryRepository.findAll();

        SaveWaterDTO waterDTO = new SaveWaterDTO();
        categories.forEach(category -> {

            switch (category.getCategoryName()) {
                case "상의":
                    waterDTO.setTop(category.getCompletedProductCount() * 2700);
                    break;
                case "아우터":
                    waterDTO.setOuter(category.getCompletedProductCount() * 8000);
                    break;
                case "바지":
                    waterDTO.setBottoms(category.getCompletedProductCount() * 11000);
                    break;
                case "스커트":
                    waterDTO.setSkirt(category.getCompletedProductCount() * 2700);
                    break;
                case "원피스":
                    waterDTO.setOne_piece(category.getCompletedProductCount() * 5000);
                    break;
                case "모자":
                    waterDTO.setHat(category.getCompletedProductCount() * 1600);
                    break;
                case "양말/레그웨어":
                    waterDTO.setSocks(category.getCompletedProductCount() * 1500);
                    break;
                case "속옷":
                    waterDTO.setUnderwear(category.getCompletedProductCount() * 1400);
                    break;
                case "액세서리":
                    waterDTO.setAccessories(category.getCompletedProductCount() * 1730);
                    break;
                default:
                    break;  // 기타 카테고리는 무시
            }
        });
/*
        SaveWaterDTO waterDTO = new SaveWaterDTO();
        waterDTO.setTop(calculateWaterUsage().getTop() * 2700);
        waterDTO.setOuter(calculateWaterUsage().getOuter() * 8000);
        waterDTO.setBottoms(calculateWaterUsage().getBottoms() * 11000);
        waterDTO.setSkirt(calculateWaterUsage().getSkirt() * 2700);
        waterDTO.setOne_piece(calculateWaterUsage().getOne_piece()*5000);
        waterDTO.setHat(calculateWaterUsage().getHat() * 1600);
        waterDTO.setSocks(calculateWaterUsage().getSocks() * 1500);
        waterDTO.setUnderwear(calculateWaterUsage().getUnderwear() * 1400);
        waterDTO.setAccessories(calculateWaterUsage().getAccessories() * 1730);
 */

        return waterDTO;
    }
}
