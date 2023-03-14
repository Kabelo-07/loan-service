package com.loans.mapper;

import com.loans.domain.Product;
import com.loans.api.api.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "amount", target = "maxAllowedLimit")
    Product toEntity(ProductDTO dto);

    @Mapping(source = "maxAllowedLimit", target = "amount")
    ProductDTO toDto(Product entity);

    List<ProductDTO> toDto(List<Product> entities);

}
