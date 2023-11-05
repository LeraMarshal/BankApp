package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.dto.product.CreateProductDTO;
import de.marshal.bankapp.dto.product.ProductDTO;
import de.marshal.bankapp.entity.Product;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.mapper.ProductMapper;
import de.marshal.bankapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<ProductDTO>> list() throws ApplicationException {
        List<Product> products = productService.findActive();

        return ResponseDTO.ok(productMapper.productListToProductDTOList(products));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<ProductDTO> create(
            @RequestBody CreateProductDTO createProductDTO
    ) {
        Product product = productService.create(
                createProductDTO.getName(),
                createProductDTO.getCurrencyCode(),
                createProductDTO.getMinInterestRate(),
                createProductDTO.getMaxOfferLimit()
        );

        return ResponseDTO.ok(productMapper.productToProductDTO(product));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<ProductDTO> delete(@PathVariable long id) throws ApplicationException {
        Product product = productService.delete(id);

        return ResponseDTO.ok(productMapper.productToProductDTO(product));
    }
}
