package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.agreement.AgreementDTO;
import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.mapper.AgreementMapper;
import de.marshal.bankapp.service.AgreementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/agreement")
public class AgreementController {
    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping
    public ResponseEntity<List<AgreementDTO>> search(@RequestParam long clientId) {
        List<Agreement> agreements = agreementService.findByClientId(clientId);

        return ResponseEntity.ok(agreementMapper.agreementListToAgreementDTOList(agreements));
    }
}
