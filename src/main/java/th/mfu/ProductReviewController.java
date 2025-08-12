package th.mfu;

import java.time.LocalDate;
import java.util.Collection;
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
public class ProductReviewController {

    @Autowired
    private ProductReviewRepository reviewRepo;

    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private CustomerRepository custRepo;

    // GET all reviews
    @GetMapping("/reviews")
    public ResponseEntity<Collection> getAllReviews() {
        return new ResponseEntity<Collection>(reviewRepo.findAll(), HttpStatus.OK);
    }

    // GET review by ID
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ProductReview> getReview(@PathVariable Long id) {
        if (!reviewRepo.existsById(id))
            return new ResponseEntity<ProductReview>(HttpStatus.NOT_FOUND);
        Optional<ProductReview> review = reviewRepo.findById(id);
        return new ResponseEntity<ProductReview>(review.get(), HttpStatus.OK);
    }

    // GET reviews by product ID
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Collection> getReviewsByProduct(@PathVariable Integer productId) {
        return new ResponseEntity<Collection>(reviewRepo.findByProductIdOrderByReviewDateDesc(productId), HttpStatus.OK);
    }

    // GET reviews by customer ID
    @GetMapping("/customers/{customerId}/reviews")
    public ResponseEntity<Collection> getReviewsByCustomer(@PathVariable Long customerId) {
        return new ResponseEntity<Collection>(reviewRepo.findByCustomerIdOrderByReviewDateDesc(customerId), HttpStatus.OK);
    }

    // GET reviews by rating
    @GetMapping("/reviews/rating/{rating}")
    public ResponseEntity<Collection> getReviewsByRating(@PathVariable Integer rating) {
        if (rating < 1 || rating > 5) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Collection>(reviewRepo.findByRating(rating), HttpStatus.OK);
    }

    // POST create a review
    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(@RequestBody ProductReview review) {
        // Validate required fields
        if (review.getProduct() == null || review.getProduct().getId() == null) {
            return new ResponseEntity<>("Product is required", HttpStatus.BAD_REQUEST);
        }
        
        if (review.getCustomer() == null || review.getCustomer().getId() == null) {
            return new ResponseEntity<>("Customer is required", HttpStatus.BAD_REQUEST);
        }

        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            return new ResponseEntity<>("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
        }

        // Validate product exists
        Optional<Product> prodOpt = prodRepo.findById(review.getProduct().getId());
        if (!prodOpt.isPresent()) {
            return new ResponseEntity<>("Product not found", HttpStatus.BAD_REQUEST);
        }

        // Validate customer exists
        Optional<Customer> custOpt = custRepo.findById(review.getCustomer().getId());
        if (!custOpt.isPresent()) {
            return new ResponseEntity<>("Customer not found", HttpStatus.BAD_REQUEST);
        }

        // Set the actual product and customer objects
        review.setProduct(prodOpt.get());
        review.setCustomer(custOpt.get());
        
        // Set review date to current date if not provided
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDate.now());
        }

        reviewRepo.save(review);
        return new ResponseEntity<>("Review created successfully", HttpStatus.CREATED);
    }

    // DELETE review by ID
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        if (!reviewRepo.existsById(id)) {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        }
        reviewRepo.deleteById(id);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.NO_CONTENT);
    }
}