package de.marshal.bankapp.dto.product;

import de.marshal.bankapp.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long id;
    private ProductStatus status;
    private String name;
    private int currencyCode;
    private int minInterestRate;
    private long maxOfferLimit;
}
