package com.huy.food.dtos.requests;

import com.huy.food.enums.MenuItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuItemRequest {
    @NotNull(message = "Section ID is required")
    private Long sectionId;

    @NotBlank(message = "Item name is required")
    private String name;

    private String image;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private MenuItemStatus status;
}

