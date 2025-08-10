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
public class CustomerController {
    public static Map<Integer, Customer> customers = new HashMap<Integer, Customer>();
    private int nextId = 1;

    @GetMapping("/customers")
    public ResponseEntity<Collection> getAllCustomers() {
        Collection results = customers.values();
        return new ResponseEntity<Collection>(results, HttpStatus.OK);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Integer id){
            if (customers.containsKey(id)){
                Customer foundCustomer = customers.get(id);
                return new ResponseEntity<Customer>(foundCustomer, HttpStatus.OK);
            }else {
                return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
            }
            
    }
    
    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer){
        customer.setId(nextId);
        customers.put(nextId, customer);
        nextId++;
        return new ResponseEntity<String>("Customer created", HttpStatus.CREATED);

    }
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id){
        customers.remove(id);
        return new ResponseEntity<String>("Customer deleted", HttpStatus.NO_CONTENT);
    }
}
