package de.marshal.bankapp.configuration;

import de.marshal.bankapp.exception.AccountNotFoundException;
import de.marshal.bankapp.exception.ProductNotFoundException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "bank")
public class BankConfigurationProperties {
    private final Long clientId;

    private final Map<Integer, Long> accountIds;

    private final Map<Integer, Long> defaultProductIds;

    public long accountIdForCurrency(int currencyCode) throws AccountNotFoundException {
        Long id = accountIds.get(currencyCode);

        if (id == null) {
            throw new AccountNotFoundException("no account for currency code " + currencyCode + " found in properties");
        }

        return id;
    }

    public long defaultProductIdForCurrency(int currencyCode) throws ProductNotFoundException {
        Long id = defaultProductIds.get(currencyCode);

        if (id == null) {
            throw new ProductNotFoundException("no default product for currency code " + currencyCode + " found in properties");
        }

        return id;
    }
}
