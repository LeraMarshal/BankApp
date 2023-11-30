package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.product.CreateProductDTO;
import de.marshal.bankapp.dto.product.ProductDTO;
import de.marshal.bankapp.entity.ProductStatus;
import de.marshal.bankapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
@AutoConfigureMockMvc
public class ProductControllerITest extends AppITests {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void findAllTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/product");
        List<ProductDTO> products = unmarshalListJson(mvcResult, ProductDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(5, products.size());
    }

    @Test
    @Transactional
    public void createTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/product", new CreateProductDTO(
                "new product",
                978,
                0,
                50000L
        ));
        ProductDTO productDTO = unmarshalJson(mvcResult, ProductDTO.class);

        // Then
        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        assertNotNull(productRepository.findById(productDTO.getId()).orElseThrow());

        assertEquals(ProductStatus.ACTIVE, productDTO.getStatus());
        assertEquals("new product", productDTO.getName());
        assertEquals(978, productDTO.getCurrencyCode());
        assertEquals(0, productDTO.getMinInterestRate());
        assertEquals(50000L, productDTO.getMaxOfferLimit());
    }

    @Test
    public void deleteTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/product/2");
        ProductDTO productDTO = unmarshalJson(mvcResult, ProductDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(ProductStatus.INACTIVE, productDTO.getStatus());
    }
}
