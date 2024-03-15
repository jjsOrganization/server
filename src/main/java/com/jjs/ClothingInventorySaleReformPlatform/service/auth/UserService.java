package com.jjs.ClothingInventorySaleReformPlatform.service.auth;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.request.LogoutDto;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.request.ReissueDto;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.request.UserLoginRequestDto;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response.SellerInfoResponse;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response.UserRoleResponse;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.DesignerUpdateDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.PurchaserUpdateDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.SellerUpdateDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.*;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.response.AuthResponseDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.SellerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorCode;

import com.jjs.ClothingInventorySaleReformPlatform.error.ErrorResponse;
import com.jjs.ClothingInventorySaleReformPlatform.error.exception.BusinessException;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.dto.TokenDto;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.provider.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

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

    /**
     * 1. 로그인 요청으로 들어온 ID, PWD 기반으로 Authentication 객체 생성
     * 2. authenticate() 메서드를 통해 요청된 Member에 대한 검증이 진행 => loadUserByUsername 메서드를 실행. 해당 메서드는 검증을 위한 유저 객체를 가져오는 부분으로써, 어떤 객체를 검증할 것인지에 대해 직접 구현
     * 3. 검증이 정상적으로 통과되었다면 인증된 Authentication객체를 기반으로 JWT 토큰을 생성
     */
    public ResponseEntity<?> login(UserLoginRequestDto login) {
        if (userRepository.findById(login.getMemberId()).orElse(null) == null) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 1. Login Email/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
        System.out.println("실제 검증 이전 authenticationToken 객체 : " + authenticationToken);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("실제 검증 이후 authentication 객체 : " + authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenDto, "로그인에 성공했습니다.", HttpStatus.OK);
    }
    /*
    public TokenDto login(String memberId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);
        log.info("Spring Security : Login ID/PW 를 기반으로 Authentication 객체 생성 success");

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Spring Security : 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분 success");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        log.info("Spring Security : 인증 정보를 기반으로 JWT 토큰 생성 success");

        return tokenDto;
    }
     */

    public ResponseEntity<?> reissue(ReissueDto reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            log.info("1. [reissue]Refresh Token 검증");
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());
        log.info("2. [reissue]Access Token에서 User email get");
        System.out.println(authentication);

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // 로그아웃되어 Redis에 RefreshToken이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            log.info("잘못된 요청입니다.");
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            log.info("Refresh Token 정보가 일치하지 않습니다.");
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        log.info("3. [reissue]redis에서 user email을 기반으로 저장된 rt 값 가져옴.");
        System.out.println(refreshToken);

        // 4. 새로운 토큰 생성
        //TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        // 4. 새로운 토큰 생성
        TokenDto tokenDto;
        try {
            tokenDto = jwtTokenProvider.generateToken(authentication);
            log.info("4. [reissue] 새로운 토큰 생성: " + tokenDto);
        } catch (Exception e) {
            log.error("토큰 생성 중 오류 발생:", e);
            return response.fail("토큰 생성 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }



        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        log.info("5. [reissue] rt redis 없데이트");

        return response.success(tokenDto, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(LogoutDto logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다");
    }






    public void joinPurchaser(PurchaserDTO purchaserDTO) {
        String email = purchaserDTO.getEmail();
        String password = purchaserDTO.getPassword();
        String rePassword = purchaserDTO.getRePassword();
        String name = purchaserDTO.getName();
        String phoneNumber = purchaserDTO.getPhoneNumber();
        String address = purchaserDTO.getAddress();

        Boolean isExistEmail = userRepository.existsByEmail(email);
        Boolean isExistPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);

        if(isExistEmail) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("email", email, "이메일이 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, fieldErrors);
        }
        if (isExistPhoneNumber) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("phoneNumber", phoneNumber, "전화번호가 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_PHONENUMBER_ALREADY_EXISTS, fieldErrors);
        }
        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        // User 엔티티 조회 또는 생성
        User user = userRepository.findById(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setName(name);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setRole("ROLE_PURCHASER");
            return userRepository.save(newUser);
        });



        // PurchaserInfo 엔티티 생성 및 설정
        PurchaserInfo purchaserInfo = new PurchaserInfo();
        purchaserInfo.setAddress(address);
        purchaserInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        purchaserRepository.save(purchaserInfo);

    }

    public AuthResponseDTO joinSeller(SellerDTO sellerDTO) {
        String email = sellerDTO.getEmail();
        String password = sellerDTO.getPassword();
        String rePassword = sellerDTO.getRePassword();
        String name = sellerDTO.getName();
        String phoneNumber = sellerDTO.getPhoneNumber();
        String storeName = sellerDTO.getStoreName();
        String storeAddress = sellerDTO.getStoreAddress();
        String businessNumber = sellerDTO.getBusinessNumber();

        Boolean isExistEmail = userRepository.existsByEmail(email);
        Boolean isExistPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);

        if(isExistEmail) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("email", email, "이메일이 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, fieldErrors);
        }
        if (isExistPhoneNumber) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("phoneNumber", phoneNumber, "전화번호가 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_PHONENUMBER_ALREADY_EXISTS, fieldErrors);
        }
        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        // User 엔티티 조회 또는 생성
        User user = userRepository.findById(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setName(name);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setRole("ROLE_SELLER");
            return userRepository.save(newUser);
        });

        // SellerInfo 엔티티 생성 및 설정
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setStoreName(storeName);
        sellerInfo.setStoreAddress(storeAddress);
        sellerInfo.setBusinessNumber(businessNumber);
        sellerInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        sellerRepository.save(sellerInfo);
        return null;
    }

    public void joinDesigner(DesignerDTO designerDTO) {
        String email = designerDTO.getEmail();
        String password = designerDTO.getPassword();
        String rePassword = designerDTO.getRePassword();
        String name = designerDTO.getName();
        String phoneNumber = designerDTO.getPhoneNumber();
        String address = designerDTO.getAddress();


        Boolean isExistEmail = userRepository.existsByEmail(email);
        Boolean isExistPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);

        if(isExistEmail) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("email", email, "이메일이 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, fieldErrors);
        }
        if (isExistPhoneNumber) {
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("phoneNumber", phoneNumber, "전화번호가 이미 존재합니다.")
            );
            throw new BusinessException(ErrorCode.USER_PHONENUMBER_ALREADY_EXISTS, fieldErrors);
        }
        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        // User 엔티티 조회 또는 생성
        User user = userRepository.findById(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(bCryptPasswordEncoder.encode(password));
            newUser.setName(name);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setRole("ROLE_DESIGNER");
            return userRepository.save(newUser);
        });

        // DesignerInfo 엔티티 생성 및 설정
        DesignerInfo designerInfo = new DesignerInfo();
        designerInfo.setAddress(address);
        designerInfo.setUser(user);  //   User 엔티티와 연결

        // 엔티티에 저장
        designerRepository.save(designerInfo);
    }

    // 판매자 정보 조회
    public SellerInfoResponse getSellerInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("사용자가 로그인되어 있지 않습니다.");
        }

        String currentUsername = authentication.getName(); // 현재 로그인한 사용자의 이메일(사용자명)을 가져옵니다.

        SellerInfo sellerInfo = sellerRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + currentUsername));

        return SellerInfoResponse.from(sellerInfo);
    }

    // 로그인한 회원 role 조회
    public UserRoleResponse getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("사용자가 로그인되어 있지 않습니다.");
        }

        String currentUsername = authentication.getName();  // 현재 로그인한 사용자의 이메일(사용자명)을 가져옵니다.

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다: " + currentUsername));

        return UserRoleResponse.from(user);
    }

    // 상품으로 판매자 이메일 조회
    public SellerInfoResponse getSellerInfoByProductId(Long productId) {
        String sellerEmail = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."))
                .getCreateBy();

        SellerInfo sellerInfo = sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + sellerEmail));

        return SellerInfoResponse.from(sellerInfo);
    }

    // 구매자 정보 수정
    public void updatePurchaser(PurchaserUpdateDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("구매자 정보를 찾을 수 없습니다: " + currentUsername));
        PurchaserInfo purchaserInfo = purchaserRepository.findById(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("구매자 추가 정보를 찾을 수 없습니다: " + currentUsername));

        String password = updateDTO.getPassword();
        String rePassword = updateDTO.getRePassword();

        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        purchaserInfo.setAddress(updateDTO.getAddress());

        userRepository.save(user);
        purchaserRepository.save(purchaserInfo);
    }

    // 판매자 정보 수정
    public void updateSeller(SellerUpdateDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("판매자 정보를 찾을 수 없습니다: " + currentUsername));
        SellerInfo sellerInfo = sellerRepository.findById(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("판매자 추가 정보를 찾을 수 없습니다: " + currentUsername));

        String password = updateDTO.getPassword();
        String rePassword = updateDTO.getRePassword();

        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        sellerInfo.setStoreName(updateDTO.getStoreName());
        sellerInfo.setStoreAddress(updateDTO.getStoreAddress());
        sellerInfo.setBusinessNumber(updateDTO.getBusinessNumber());

        userRepository.save(user);
        sellerRepository.save(sellerInfo);
    }

    // 디자이너 정보 수정
    public void updateDesigner(DesignerUpdateDTO updateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findById(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("디자이너 정보를 찾을 수 없습니다: " + currentUsername));
        DesignerInfo designerInfo = designerRepository.findById(currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("디자이너 추가 정보를 찾을 수 없습니다: " + currentUsername));

        String password = updateDTO.getPassword();
        String rePassword = updateDTO.getRePassword();

        if (!password.equals(rePassword)) {  // 비밀번호 중복 검사
            List<ErrorResponse.FieldError> fieldErrors = List.of(
                    new ErrorResponse.FieldError("rePassword", rePassword, "비밀번호가 일치하지 않습니다.")
            );
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH, fieldErrors);
        }

        user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        designerInfo.setAddress(updateDTO.getAddress());

        userRepository.save(user);
        designerRepository.save(designerInfo);
    }

}
