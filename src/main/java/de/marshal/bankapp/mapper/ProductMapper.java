package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.product.ProductDTO;
import de.marshal.bankapp.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productListToProductDTOList(List<Product> products);
}
