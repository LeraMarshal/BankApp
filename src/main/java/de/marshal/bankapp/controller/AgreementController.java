package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.dto.agreement.AgreementDTO;
import de.marshal.bankapp.dto.agreement.CreateAgreementDTO;
import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.mapper.AgreementMapper;
import de.marshal.bankapp.service.AgreementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/agreement")
public class AgreementController {
    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<AgreementDTO>> search(@RequestParam long clientId) {
        List<Agreement> agreements = agreementService.findByClientId(clientId);

        return ResponseDTO.ok(agreementMapper.agreementListToAgreementDTOList(agreements));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<AgreementDTO> create(
            @RequestBody CreateAgreementDTO createAgreementDTO
    ) throws ApplicationException {
        Agreement agreement = agreementService.create(
                createAgreementDTO.getAccountId(),
                createAgreementDTO.getProductId(),
                createAgreementDTO.getInterestRate(),
                createAgreementDTO.getAmount()
        );

        return ResponseDTO.ok(agreementMapper.agreementToAgreementDTO(agreement));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<AgreementDTO> get(@PathVariable long id) throws ApplicationException {
        Agreement agreement = agreementService.getById(id);

        return ResponseDTO.ok(agreementMapper.agreementToAgreementDTO(agreement));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<AgreementDTO> delete(@PathVariable long id) throws ApplicationException {
        Agreement agreement = agreementService.deleteById(id);

        return ResponseDTO.ok(agreementMapper.agreementToAgreementDTO(agreement));
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<AgreementDTO> confirm(@PathVariable long id) throws  ApplicationException {
        Agreement agreement = agreementService.confirm(id);

        return ResponseDTO.ok(agreementMapper.agreementToAgreementDTO(agreement));
    }
}
