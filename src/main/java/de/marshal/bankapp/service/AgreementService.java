package de.marshal.bankapp.service;

import de.marshal.bankapp.configuration.BankConfigurationProperties;
import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.*;
import de.marshal.bankapp.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {
    private final BankConfigurationProperties bankConfigurationProperties;
    private final AgreementRepository agreementRepository;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final ProductService productService;

    public List<Agreement> findByClientId(long clientId) {
        return agreementRepository.findByClientId(clientId);
    }

    public Agreement getById(long agreementId) throws AgreementNotFoundException {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new AgreementNotFoundException("agreement with id " + agreementId + " not found"));
    }

    @Transactional
    public Agreement deleteById(long agreementId) throws AgreementNotFoundException, InvalidStatusException,
            UnfulfilledObligationsException {
        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new AgreementNotFoundException("agreement with id " + agreementId + " not found"));

        if (agreement.getStatus() != AgreementStatus.ACTIVE) {
            throw new InvalidStatusException("agreement with id " + agreementId + " is not ACTIVE");
        }

        Long debt = agreement.getDebt();
        if (debt != 0L) {
            throw new UnfulfilledObligationsException("there must be no debt to delete agreement, current debt is " + debt);
        }

        log.info("Deleting agreement, clientId=[{}], agreementId=[{}]",
                agreement.getAccount().getClient().getId(), agreement.getId());

        agreement.setStatus(AgreementStatus.INACTIVE);
        agreementRepository.save(agreement);

        return agreement;
    }

    @Transactional
    public Agreement create(Account account, Product product) throws InvalidInterestRateException,
            InvalidAmountException, InvalidStatusException, CurrencyCodeMismatchException {
        return create(account, product, 0, 0L);
    }

    @Transactional
    public Agreement create(
            long accountId,
            long productId,
            int interestRate,
            long amount
    ) throws AccountNotFoundException, ProductNotFoundException, InvalidInterestRateException,
            InvalidAmountException, InvalidStatusException, CurrencyCodeMismatchException {
        Account account = accountService.getById(accountId);
        Product product = productService.getById(productId);

        return create(account, product, interestRate, amount);
    }

    @Transactional
    public Agreement create(
            Account account,
            Product product,
            int interestRate,
            long amount
    ) throws InvalidInterestRateException, CurrencyCodeMismatchException, InvalidAmountException, InvalidStatusException {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("account status must be ACTIVE to create new agreement");
        }
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new InvalidStatusException("product status must be ACTIVE to create new agreement");
        }
        if (!product.getCurrencyCode().equals(account.getCurrencyCode())) {
            throw new CurrencyCodeMismatchException("product currency code " + product.getCurrencyCode()
                    + " does not match account currency code " + account.getCurrencyCode());
        }

        int minInterestRate = product.getMinInterestRate();
        if (interestRate < minInterestRate || (minInterestRate < 0 && interestRate > 0)) {
            throw new InvalidInterestRateException("interest rate must be greater than " + minInterestRate);
        }

        long maxAmount = product.getMaxOfferLimit();
        if (amount > maxAmount) {
            throw new InvalidAmountException("amount must be less than " + maxAmount);
        }

        log.info("Creating agreement, clientId=[{}], productId=[{}]", account.getClient().getId(), product.getId());

        Agreement agreement = new Agreement(
                account,
                product,
                AgreementStatus.PENDING,
                interestRate,
                (interestRate > 0) ? amount : -amount
        );

        agreementRepository.save(agreement);

        return agreement;
    }

    @Transactional
    public Agreement confirm(long agreementId) throws AgreementNotFoundException, InsufficientFundsException,
            AccountNotFoundException, CurrencyCodeMismatchException, InvalidStatusException {
        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new AgreementNotFoundException("agreement with id " + agreementId + " not found"));

        return confirm(agreement);
    }

    @Transactional
    public Agreement confirm(Agreement agreement) throws AccountNotFoundException, InsufficientFundsException,
            CurrencyCodeMismatchException, InvalidStatusException {
        if (agreement.getStatus() != AgreementStatus.PENDING) {
            throw new InvalidStatusException("agreement status must be PENDING to confirm");
        }

        long bankAccountId = bankConfigurationProperties.accountIdForCurrency(agreement.getProduct().getCurrencyCode());
        Account bankAccount = accountService.getById(bankAccountId);

        log.info("Confirming agreement, clientId=[{}], agreementId=[{}]",
                agreement.getAccount().getClient().getId(), agreement.getId());

        if (agreement.getInterestRate() > 0) {
            transactionService.create(
                    bankAccount,
                    agreement.getAccount(),
                    "Confirmed agreement",
                    agreement.getDebt()
            );
        } else {
            transactionService.create(
                    agreement.getAccount(),
                    bankAccount,
                    "Confirmed agreement",
                    -agreement.getDebt()
            );
        }

        agreement.setStatus(AgreementStatus.ACTIVE);

        agreementRepository.save(agreement);

        return agreement;
    }
}
