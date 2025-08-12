package th.mfu;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleOrderController {

    @Autowired
    private SaleOrderRepository saleOrderRepo;

    @Autowired
    private CustomerRepository custRepo;

    @Autowired
    private ProductRepository prodRepo;

    @GetMapping("/orders")
    public ResponseEntity<Collection> getAllOrders(){
        return new ResponseEntity<Collection>(saleOrderRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody SaleOrder order){
        if(order.getCustomer()==null || order.getCustomer().getId()==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Customer> custOpt = custRepo.findById(order.getCustomer().getId());
        if (!custOpt.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        }
        order.setCustomer(custOpt.get());
        order.setOrderDate(LocalDate.now());

        double totalAmount = 0.0;
        for(SaleOrderItem item : order.getItems()){
            if(item.getProduct()==null || item.getProduct().getId()==null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Optional<Product> prodOpt = prodRepo.findById(item.getProduct().getId());
            if(!prodOpt.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }
            Product product = prodOpt.get();
            item.setProduct(product);
            item.setPrice(product.getPrice()*item.getQuantity());

            totalAmount += item.getPrice();

        }
        order.setTotalAmount(totalAmount);
        saleOrderRepo.save(order);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}