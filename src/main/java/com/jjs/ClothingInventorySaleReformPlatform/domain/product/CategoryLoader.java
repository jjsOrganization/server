package com.jjs.ClothingInventorySaleReformPlatform.domain.product;

import com.jjs.ClothingInventorySaleReformPlatform.repository.product.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 카테고리 데이터 생성
        List<Category> categories = Arrays.asList(
                new Category("상의"),
                new Category("아우터"),
                new Category("바지"),
                new Category("스커트"),
                new Category("원피스"),
                new Category("모자"),
                new Category("양말/레그웨어"),
                new Category("속옷"),
                new Category("액세서리")
        );

        // 카테고리 데이터가 존재하는지 확인 후 없으면 생성
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(categories);
        }
    }
}
