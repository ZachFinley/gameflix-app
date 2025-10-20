package psu.edu.GameFlix.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import psu.edu.GameFlix.Models.Payment;
import psu.edu.GameFlix.Models.UserSubscription;
import psu.edu.GameFlix.Repoitories.PaymentRepository;
import psu.edu.GameFlix.Repoitories.UserRepository;
import psu.edu.GameFlix.Repoitories.UserSubscriptionRepository;

@Service
public class PaymentService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository thePaymentRepository, UserSubscriptionRepository theUserSubscriptionRepository, UserRepository theUserRepository) {
        paymentRepository = thePaymentRepository;
        userSubscriptionRepository = theUserSubscriptionRepository;
        userRepository = theUserRepository;
    }

    public List<Payment> findUserPayments(int userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Optional<Payment> findPaymentById(int paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public List<Payment> findPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status.trim());
    }

    public List<Payment> findUserPaymentsByStatus(int userId, String status) {
        return paymentRepository.findByUserIdAndStatus(userId, status.trim());
    }

    public List<Payment> findSuccessfulUserPayments(int userId) {
        return findUserPaymentsByStatus(userId, "Succeeded");
    }

    public List<Payment> findFailedUserPayments(int userId) {
        return findUserPaymentsByStatus(userId, "Failed");
    }

    public List<UserSubscription> findUserSubscriptions(int userId) {
        return userSubscriptionRepository.findByUserId(userId);
    }

    public List<UserSubscription> findActiveUserSubscriptions(int userId) {
        return userSubscriptionRepository.findByStatus("Active");
    }

    public double calculateTotalUserPayments(int userId) {
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p " +
                        "WHERE p.userId = :userId AND p.status = 'Succeeded'",
                Double.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public List<Payment> findRecentPayments(int userId) {
        TypedQuery<Payment> query = entityManager.createQuery(
                "SELECT p FROM Payment p " + "WHERE p.userId = :userId " +
                        "AND p.createdAt >= :thirtyDaysAgo " + "ORDER BY p.createdAt DESC",
                Payment.class);
        query.setParameter("userId", userId);
        query.setParameter("thirtyDaysAgo", LocalDateTime.now().minusDays(30));
        return query.getResultList();
    }

    public Payment processPayment(Payment payment) {
        validatePayment(payment);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public UserSubscription createUserSubscription(UserSubscription subscription) {
        validateUserSubscription(subscription);
        subscription.setCreatedAt(LocalDateTime.now());

        return userSubscriptionRepository.save(subscription);
    }

    private boolean isValidPaymentStatus(String status) {
        return status.equals("Succeeded") || status.equals("Failed") ||
                status.equals("Refunded") || status.equals("Pending");
    }

    private void validatePayment(Payment payment) {
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (payment.getStatus() == null || payment.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment status cannot be null or empty");
        }
        if (!isValidPaymentStatus(payment.getStatus())) {
            throw new IllegalArgumentException("Invalid payment status: " + payment.getStatus());
        }

        if (!userRepository.existsById(payment.getUserId())) {
            throw new IllegalArgumentException("User with ID " + payment.getUserId() + " does not exist");
        }
    }

    private void validateUserSubscription(UserSubscription subscription) {
        if (subscription.getStatus() == null || subscription.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Subscription status cannot be null or empty");
        }

        if (!userRepository.existsById(subscription.getUserId())) {
            throw new IllegalArgumentException("User with ID " + subscription.getUserId() + " does not exist");
        }
    }
}
