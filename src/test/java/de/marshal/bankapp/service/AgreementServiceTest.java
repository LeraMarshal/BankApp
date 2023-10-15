package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.repository.AgreementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class AgreementServiceTest {
    @MockBean
    private AgreementRepository agreementRepository;

    private AgreementService agreementService;

    @BeforeEach
    public void setup() {
        agreementService = new AgreementService(agreementRepository);
    }

    @Test
    public void findByClientIdTest() {
        Mockito.when(agreementRepository.findByClientId(1L)).thenReturn(List.of(new Agreement()));
        Assertions.assertEquals(1, agreementService.findByClientId(1L).size());
        Mockito.verify(agreementRepository).findByClientId(1L);
    }
}
