package com.jjs.ClothingInventorySaleReformPlatform.controller.designer;

import com.jjs.ClothingInventorySaleReformPlatform.controller.product.AuthenticationFacade;
import com.jjs.ClothingInventorySaleReformPlatform.domain.Portfolio;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.designer.PortfolioInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResultCode;
import com.jjs.ClothingInventorySaleReformPlatform.response.ResultResponse;
import com.jjs.ClothingInventorySaleReformPlatform.service.designer.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "포트폴리오 컨트롤러", description = "포트폴리오 API 입니다.")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "포트폴리오 등록", description = "입력한 포트폴리오 정보를 저장합니다.")
    @PostMapping("/portfolio/designer")
    public ResponseEntity<Object> uploadPortfolio(@Valid @ModelAttribute PortfolioDTO portfolioDTO,
                                                  BindingResult bindingResult) throws IOException {

        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        ResponseEntity<Object> imageErrorResponse = getObjectResponseEntity(portfolioDTO.getDesignerImage().isEmpty(),
                bindingResult, ErrorCode.IMAGE_EMPTY);
        if (imageErrorResponse != null) return imageErrorResponse;

        try {
            portfolioDTO.setDesignerEmail(authenticationFacade.getCurrentUsername());
            portfolioService.savePortfolio(portfolioDTO);

            // 수정 필요한 부분
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.REGISTER_PORTFOLIO_SUCCESS,
                    Collections.singletonMap("DesignerEmail", portfolioDTO.getDesignerEmail()));
            //
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);

        } catch (Exception e) { // service에서 throw된 에러 메세지 반환
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * 디자이너 이메일을 통해 포트폴리오 조회 메소드
     *
     * @return
     */

    @Operation(summary = "포트폴리오 조회(로그인)", description = "로그인 된 디자이너의 포트폴리오 정보를 조회합니다.")
    @GetMapping("/portfolio/designer")
    public ResponseEntity<Object> loadPortfolio() throws IOException {

        String designerEmail = authenticationFacade.getCurrentUsername(); // 로그인되어 있는 유저의 이메일

        Optional<PortfolioInfoDTO> portfolioInfo = portfolioService.getPortfolio(designerEmail);

        return portfolioInfo.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "포트폴리오 수정", description = "디자이너의 포트폴리오를 수정합니다.")
    @PutMapping("/portfolio/designer/{portfolio_id}")
    public ResponseEntity<Object> modifyPortfolio(@Valid @ModelAttribute PortfolioDTO portfolioDTO, BindingResult bindingResult,
                                                  @PathVariable Long portfolio_id) throws IOException {

        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        try {
            portfolioDTO.setID(portfolio_id); // 수정할 포트폴리오의 id 셋팅

            portfolioService.updatePortfolio(portfolioDTO); // update 실행

            // 수정 필요한 부분
            ResultResponse resultResponse = ResultResponse.of(AuthResultCode.MODIFY_PORTFOLIO_SUCCESS,
                    Collections.singletonMap("DesignerEmail", portfolioDTO.getDesignerEmail()));
            //
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "디자이너 검색(키워드)", description = "디자이너 전체 조회에 대하여 특정 검색어가 포함된 디자이너가 조회된다.")
    @GetMapping("/portfolio/{keyword}")
    public ResponseEntity<List<PortfolioInfoDTO>> getPortfoliosByName(@PathVariable String keyword) {
        List<PortfolioInfoDTO> searchedDesigner = portfolioService.getPortfolioByName(keyword);
        System.out.println("searchedDesigner = " + searchedDesigner);
        return ResponseEntity.ok(searchedDesigner);
    }

    @Operation(summary = "전체 디자이너 조회", description = "모든 회원은 디자이너 검색 가능")
    @GetMapping("/portfolio/all")
    public ResponseEntity<List<PortfolioInfoDTO>> getAllPortfolio() throws IOException {
        List<PortfolioInfoDTO> portfolios = portfolioService.getAllPortfolio();
        return ResponseEntity.ok(portfolios);
    }

    /**
     * 선택한 디자이너의 포트폴리오 상세정보를 조회하는 메소드
     * @param portfolioid
     * @param bindingResult
     * @return
     */

    @Operation(summary = "선택한 디자이너 포트폴리오 조회", description = "디자이너의 포트폴리오 상세 검색")
    @GetMapping("/portfolio/{portfolioid}/detail")
    public ResponseEntity<Object> getPortfolioDetail(@PathVariable Long portfolioid, BindingResult bindingResult){
        Portfolio portfolioById = portfolioService.getPortfolioById(portfolioid).get();
        PortfolioInfoDTO portfolioInfoDTO = new PortfolioInfoDTO();

        ResponseEntity<Object> errorResponse = getObjectResponseEntity(bindingResult.hasErrors(),
                bindingResult, ErrorCode.INVALID_BAD_REQUEST);
        if (errorResponse != null) return errorResponse;

        try {
            portfolioInfoDTO.setDesignerImagePath(portfolioById.getDesignerImage());
            portfolioInfoDTO.setDesignerName(portfolioById.getName());
            portfolioInfoDTO.setExplanation(portfolioById.getExplanation());

            return new ResponseEntity<>(portfolioInfoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * 에러 메세지 생성하는 메소드
     *
     * @param bindingResult
     * @param bindingResult1
     * @param invalidBadRequest
     * @return
     */

    private static ResponseEntity<Object> getObjectResponseEntity(boolean bindingResult, BindingResult bindingResult1, ErrorCode invalidBadRequest) {
        if (bindingResult) {
            List<ErrorResponse.FieldError> fieldErrors = bindingResult1.getFieldErrors().stream()
                    .map(fieldError -> new ErrorResponse.FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            ErrorResponse errorResponse = new ErrorResponse(invalidBadRequest, fieldErrors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
