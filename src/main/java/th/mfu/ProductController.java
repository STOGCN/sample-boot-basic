package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepo;

 @GetMapping("/products")
    public ResponseEntity<Collection> getAllProduct(){
        Collection results = productRepo.findAll();
        return new ResponseEntity<Collection>(results, HttpStatus.OK);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){
            if (productRepo.existsById(id)){
                Optional<Product> foundCustomer = productRepo.findById(id);
                return new ResponseEntity<Product>(foundCustomer.get(), HttpStatus.OK);
            }else {
                return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
            }
            
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        productRepo.save(product);
        return new ResponseEntity<String>("product created", HttpStatus.CREATED);

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
       productRepo.deleteById(id);
        return new ResponseEntity<String>("product deleted", HttpStatus.NO_CONTENT);
    }
}
