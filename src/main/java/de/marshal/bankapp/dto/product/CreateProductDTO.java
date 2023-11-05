package de.marshal.bankapp.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDTO {
    private String name;
    private int currencyCode;
    private int minInterestRate;
    private long maxOfferLimit;
}
