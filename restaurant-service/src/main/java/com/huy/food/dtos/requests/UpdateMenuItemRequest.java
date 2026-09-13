package com.huy.food.dtos.requests;

import com.huy.food.enums.MenuItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenuItemRequest {
    private Long sectionId;
    private String name;
    private String image;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private MenuItemStatus status;

    private Long currentDisplayOrder;
    private Long newPreviousDisplayOrder;
    private Long newNextDisplayOrder;
}

