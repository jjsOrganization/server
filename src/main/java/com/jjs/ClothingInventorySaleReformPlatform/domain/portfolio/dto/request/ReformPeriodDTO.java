package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReformPeriodDTO {

    private LocalDate requestStartDate;
    private LocalDate completeDate;
}
