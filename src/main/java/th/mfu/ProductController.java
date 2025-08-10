package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public static Map<Integer,Product> products = new HashMap<Integer,Product>();
    private int nextId = 1;//
///////////////////////////////////private int nextId = 1;
 @GetMapping("/products")
    public ResponseEntity<Collection> getAllProduct(){
        Collection results = products.values();
        return new ResponseEntity<Collection>(results, HttpStatus.OK);
    }


    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id){
            if (products.containsKey(id)){
                Product foundCustomer = products.get(id);
                return new ResponseEntity<Product>(foundCustomer, HttpStatus.OK);
            }else {
                return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
            }
            
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        product.setId(nextId);
        products.put(nextId, product);
        nextId++;
        return new ResponseEntity<String>("product created", HttpStatus.CREATED);

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        products.remove(id);
        return new ResponseEntity<String>("product deleted", HttpStatus.NO_CONTENT);
    }
}
