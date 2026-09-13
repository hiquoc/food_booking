package com.huy.food.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRestaurantSectionRequest {
    private String name;
    private Long currentDisplayOrder;
    private Long newPreviousDisplayOrder;
    private Long newNextDisplayOrder;
}

