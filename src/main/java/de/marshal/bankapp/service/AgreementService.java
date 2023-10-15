package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {
    private final AgreementRepository agreementRepository;

    public List<Agreement> findByClientId(long clientId) {
        return agreementRepository.findByClientId(clientId);
    }
}
