package psu.edu.GameFlix.Repoitories;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Standard JPA methods work automatically with your Payment entity
    List<Payment> findByUserId(int userId);

    List<Payment> findByStatus(String status);

    List<Payment> findBySubscriptionId(int subscriptionId);

    List<Payment> findByUserIdAndStatus(int userId, String status);

    // All other standard methods inherited from JpaRepository:
    // List<Payment> findAll()
    // Optional<Payment> findById(Integer id)
    // Payment save(Payment payment)
    // void deleteById(Integer id)
    // boolean existsById(Integer id)
}
