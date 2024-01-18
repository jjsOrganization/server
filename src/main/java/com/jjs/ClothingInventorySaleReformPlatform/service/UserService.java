package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import com.jjs.ClothingInventorySaleReformPlatform.dto.UserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.PurchaserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PurchaserRepository purchaserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        String name = userDTO.getName();
        String phoneNumber = userDTO.getPhoneNumber();

        Boolean isExist = purchaserRepository.existsByEmail(email);

        if(isExist) {
            return;
        }

        User data = new User();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setName(name);
        data.setPhoneNumber(phoneNumber);
        data.setRole("ROLE_USER");

        purchaserRepository.save(data);
    }



    /*
    @Transactional
    public void save(PurchaserDTO purchaserDTO) {
        Purchaser purchaser = Purchaser.toPurchaser(purchaserDTO);
        validateDuplicatePurchaserEmail(purchaser); // 입력한 이메일이 이미 db에 존재하는지 검사
        validateDuplicatePurchaserPhoneNumber(purchaser); // 입력한 전화번호가 이미 db에 존재하는지 검사
        validateDuplicatePurchaserNickname(purchaser); // 입력한 닉네임이 이미 db에 존재하는지 검사
        purchaserRepository.save(purchaser);
    }

    public String login(PurchaserDTO purchaserDTO) {
        Optional<Purchaser> parchaserByEmail = purchaserRepository.findByEmail(purchaserDTO.getEmail()); // 이메일로 DB에서 회원 조회
        if (parchaserByEmail.isPresent()) {
            Purchaser purchaser = parchaserByEmail.get(); // 로그인 된 사용자 정보
            if (purchaser.getPassword().equals(purchaserDTO.getPassword())) {
                return PurchaserDTO.toPurchaserDTO(purchaser).getEmail(); // 로그인된 유저의 이메일만 넘김
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }
    }

    private void validateDuplicatePurchaserEmail(Purchaser purchaser) {
        Optional<Purchaser> purchaserByEmail = purchaserRepository.findByEmail(purchaser.getEmail());
        if(purchaserByEmail.isPresent()){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }


    private void validateDuplicatePurchaserPhoneNumber(Purchaser purchaser) {
        Optional<Purchaser> purchaserByPhoneNumber = purchaserRepository.findByPhoneNumber(purchaser.getPhoneNumber());
        if(purchaserByPhoneNumber.isPresent()){
            throw new IllegalStateException("이미 존재하는 전화번호입니다.");
        }
    }
    private void validateDuplicatePurchaserNickname(Purchaser purchaser) {
        Optional<Purchaser> purchaserByNickName = purchaserRepository.findByNickname(purchaser.getNickname());
        if(purchaserByNickName.isPresent() ){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

     */
}
