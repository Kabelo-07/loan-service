package com.loans.mapper;

import com.loans.api.api.model.LoanAccountDTO;
import com.loans.domain.LoanAccount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanAccountMapper {

    LoanAccountMapper INSTANCE = Mappers.getMapper(LoanAccountMapper.class);

    LoanAccount toEntity(LoanAccountDTO dto);

    LoanAccountDTO toDto(LoanAccount entity);

}
