package th.mfu;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProductReviewRepository extends CrudRepository<ProductReview, Long> {
    List<ProductReview> findAll();
    List<ProductReview> findByProductId(Integer productId);
    List<ProductReview> findByCustomerId(Long customerId);
    List<ProductReview> findByRating(Integer rating);
    List<ProductReview> findByProductIdOrderByReviewDateDesc(Integer productId);
    List<ProductReview> findByCustomerIdOrderByReviewDateDesc(Long customerId);
}