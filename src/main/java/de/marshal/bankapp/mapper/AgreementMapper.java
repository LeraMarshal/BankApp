package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.agreement.AgreementDTO;
import de.marshal.bankapp.entity.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(target = "productId", source="product.id")
    AgreementDTO agreementToAgreementDTO(Agreement agreement);

    List<AgreementDTO> agreementListToAgreementDTOList(List<Agreement> agreements);
}
