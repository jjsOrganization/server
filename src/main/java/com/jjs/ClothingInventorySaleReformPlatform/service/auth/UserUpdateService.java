package com.jjs.ClothingInventorySaleReformPlatform.service.auth;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.patchUser.*;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.error.exception.BusinessException;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.provider.JwtTokenProvider;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.DesignerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.SellerRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.UserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserUpdateService {

    private final PurchaserRepository purchaserRepository;
    private final SellerRepository sellerRepository;
    private final DesignerRepository designerRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProductRepository productRepository;

    private final RedisTemplate redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Response response;

    // 전화번호 수정 -> User - Purchaser, Seller, Designer
    public void updatePhoneNumber(UpdatePhoneNumberDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("구매자 정보를 찾을 수 없습니다: " + currentUsername));

        user.setPhoneNumber(updateDTO.getPhoneNumber());
        userRepository.save(user);
    }

    // 비밀번호 변경 -> User - Purchaser, Seller, Designer
    public void updatePassword(UpdatePasswordDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("구매자 정보를 찾을 수 없습니다: " + currentUsername));

        String password = updateDTO.getPassword();
        String rePassword = updateDTO.getRePassword();

        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
        userRepository.save(user);
    }

    // 주소 수정 -> Purchaser
    public void updatePurchaserAddress(UpdateAddressDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        PurchaserInfo purchaserInfo = purchaserRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("구매자 정보를 찾을 수 없습니다: " + currentUsername));

        purchaserInfo.setAddress(updateDTO.getAddress());
        purchaserRepository.save(purchaserInfo);
    }

    // 매장명 수정 -> Seller
    public void updateSellerStoreName(UpdateStoreNameDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        SellerInfo sellerInfo = sellerRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + currentUsername));

        sellerInfo.setStoreName(updateDTO.getStoreName());
        sellerRepository.save(sellerInfo);
    }

    // 매장주소 수정 -> Seller
    public void updateSellerStoreAddress(UpdateStoreAddressDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        SellerInfo sellerInfo = sellerRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + currentUsername));

        sellerInfo.setStoreAddress(updateDTO.getStoreAddress());
        sellerRepository.save(sellerInfo);
    }

    // 사업자 주소 수정 -> Seller
    public void updateSellerBusinessNumber(UpdateBusinessNumberDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        SellerInfo sellerInfo = sellerRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + currentUsername));

        sellerInfo.setBusinessNumber(updateDTO.getBusinessNumber());
        sellerRepository.save(sellerInfo);
    }

    // 주소 수정 -> Designer
    public void updateDesignerAddress(UpdateAddressDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        DesignerInfo designerInfo = designerRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("디자이너 정보를 찾을 수 없습니다: " + currentUsername));

        designerInfo.setAddress(updateDTO.getAddress());
        designerRepository.save(designerInfo);
    }
}
