package com.jjs.ClothingInventorySaleReformPlatform.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Designer;
import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import com.jjs.ClothingInventorySaleReformPlatform.dto.DesignerDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.PurchaserDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ToString
public class DesignerService {
    private final DesignerRepository designerRepository;


    public void save(DesignerDTO designerDTO) {
        Designer designer = Designer.toDesigner(designerDTO);
        validateDuplicateDesignerEmail(designer); // 입력한 이메일이 이미 db에 존재하는지 검사
        validateDuplicateDesignerNickname(designer); // 입력한 전화번호가 이미 db에 존재하는지 검사
        validateDuplicateDesignerPhoneNumber(designer); // 입력한 닉네임이 이미 db에 존재하는지 검사

        designerRepository.save(designer);
    }

    public String login(DesignerDTO designerDTO) {
        Optional<Designer> DesignerByEmail = designerRepository.findByEmail(designerDTO.getEmail());
        if(DesignerByEmail.isPresent()){
            Designer designer = DesignerByEmail.get(); // 여기서 entity로 조회해도 되는건가??
            if (designer.getPassword().equals(designerDTO.getPassword())) {
                return DesignerDTO.toDesignerDTO(designer).getEmail();
            }
            return null;
        }
        return null;
    }

    private void validateDuplicateDesignerEmail(Designer designer) {
        Optional<Designer> designerByEmail = designerRepository.findByEmail(designer.getEmail());
        if(designerByEmail.isPresent()){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }


    private void validateDuplicateDesignerPhoneNumber(Designer designer) {
        Optional<Designer> designerByPhoneNumber = designerRepository.findByPhoneNumber(designer.getPhoneNumber());
        if(designerByPhoneNumber.isPresent()){
            throw new IllegalStateException("이미 존재하는 전화번호입니다.");
        }
    }
    private void validateDuplicateDesignerNickname(Designer designer) {
        Optional<Designer> designerByDesignerName = designerRepository.findByDesignerName(designer.getDesignerName());
        if(designerByDesignerName.isPresent() ){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
}
