package com.jjs.ClothingInventorySaleReformPlatform.controller.designer;

import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.service.designer.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    @PostMapping("/designer/portfolio")
    public ResponseEntity<Object> uploadPortfolio(@ModelAttribute PortfolioDTO portfolioDTO) throws IOException {

        portfolioService.savePortfolio(portfolioDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
