package com.huy.food.mappers;
import com.huy.food.dtos.requests.*; import com.huy.food.dtos.responses.OtpResponse; import com.huy.food.entities.Otp; import org.mapstruct.*;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OtpMapper { Otp toEntity(CreateOtpRequest request); OtpResponse toResponse(Otp otp); void update(UpdateOtpRequest request, @MappingTarget Otp otp); }
