package th.mfu;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductControllerTest {

    @Mock
    private ProductRepository prodRepo;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        product1 = new Product();
        product1.setId(1);
        product1.setDescription("Laptop");
        product1.setPrice(1500.0);

        product2 = new Product();
        product2.setId(2);
        product2.setDescription("Phone");
        product2.setPrice(800.0);
    }

    @Test
    void testGetProduct_Found() {
        when(prodRepo.existsById(1)).thenReturn(true);
        when(prodRepo.findById(1)).thenReturn(Optional.of(product1));

        ResponseEntity<Product> response = productController.getProduct(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(product1);
    }

    @Test
    void testGetProduct_NotFound() {
        when(prodRepo.existsById(99)).thenReturn(false);

        ResponseEntity<Product> response = productController.getProduct(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetAllProducts() {
        when(prodRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<java.util.Collection> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(product1, product2);
    }

    @Test
    void testSearchByDescription() {
        when(prodRepo.findByDescriptionContaining("Lap")).thenReturn(Arrays.asList(product1));

        ResponseEntity<java.util.Collection> response = productController.searchByDescription("Lap");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(product1);
    }

    @Test
    void testListByPrice() {
        when(prodRepo.findByOrderByPrice()).thenReturn(Arrays.asList(product2, product1));

        ResponseEntity<java.util.Collection> response = productController.listByPrice();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(product2, product1);
    }

    @Test
    void testCreateProduct() {
        when(prodRepo.save(product1)).thenReturn(product1);

        ResponseEntity<String> response = productController.createProduct(product1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("Product created");
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(prodRepo).deleteById(1);

        ResponseEntity<String> response = productController.deleteProduct(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEqualTo("Product deleted");
    }
}
