package com.jjs.ClothingInventorySaleReformPlatform.controller.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestCheckDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.estimate.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EstimateController {

    private final Response response;
    private final EstimateService estimateService;

    @GetMapping("/estimate/designer/requestForm")
    public ResponseEntity<?> getRequestForm() {
        List<ReformRequestCheckDTO> reformRequestCheckDTOList = estimateService.getRequestList();

        return response.success(reformRequestCheckDTOList);
    }

    @PostMapping("/estimate/designer/requestForm")
    public ResponseEntity<?> updateRequestStatus() {
        return null;
    }
}

