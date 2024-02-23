package com.jjs.ClothingInventorySaleReformPlatform.service.reformrequest;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequestImage;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.reformrequest.ReformRequestDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.DesignerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestImgRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.reformrequest.ReformRequestRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReformRequestService {
    private final S3Service s3Service;

    private final ReformRequestRepository reformRequestRepository;
    private final ReformRequestImgRepository reformRequestImgRepository;

    private final ProductRepository productRepository;
    private final PurchaserRepository purchaserRepository;
    private final DesignerRepository designerRepository;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    @Transactional
    public ReformProductInfoDTO getProductInfo(Long itemId) {
        ReformProductInfoDTO reformProductInfoDTO = new ReformProductInfoDTO(productRepository.findProductById(itemId));

        return reformProductInfoDTO;
    }

    @Transactional
    public void saveReformRequest(ReformRequestDTO reformRequestDTO, Long itemId) throws Exception{
        // 구매자와 디자이너의 이메일 삽입을 위한 객체 생성
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(getCurrentUsername());
        DesignerInfo designerInfo = designerRepository.findDesignerByEmail(reformRequestDTO.getDesignerEmail());

        // 의뢰서 등록
        ReformRequest reformRequest = new ReformRequest();
        reformRequest.setRequestInfo(reformRequestDTO.getRequestInfo());
        reformRequest.setRequestPart(reformRequestDTO.getRequestPart());
        reformRequest.setRequestPrice(reformRequestDTO.getRequestPrice());

        reformRequest.setClientEmail(purchaserInfo);
        reformRequest.setDesignerEmail(designerInfo);
        reformRequest.setProductNumber(productRepository.findProductById(itemId));
        reformRequestRepository.save(reformRequest);

        // 첨부 이미지 등록
        List<MultipartFile> requestImg = reformRequestDTO.getRequestImg();


        for (MultipartFile img : requestImg) {
            ReformRequestImage reformRequestImage = new ReformRequestImage();
            reformRequestImage.setImgUrl(s3Service.uploadFile(img, "ReformRequestImage/"));
            reformRequestImage.setReformRequest(reformRequest);
            reformRequestImgRepository.save(reformRequestImage);
        }

    }

    public void getReformRequestForm(){

    }
}
