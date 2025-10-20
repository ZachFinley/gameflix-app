package psu.edu.GameFlix.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import psu.edu.GameFlix.Models.Payment;
import psu.edu.GameFlix.Models.UserSubscription;
import psu.edu.GameFlix.Services.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable int userId) {
        try {
            List<Payment> payments = paymentService.findUserPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable int paymentId) {
        try {
            return paymentService.findPaymentById(paymentId)
                    .map(payment -> ResponseEntity.ok(payment))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable String status) {
        try {
            List<Payment> payments = paymentService.findPaymentsByStatus(status);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<Payment>> getUserPaymentsByStatus(
            @PathVariable int userId,
            @PathVariable String status) {
        try {
            List<Payment> payments = paymentService.findUserPaymentsByStatus(userId, status);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/successful")
    public ResponseEntity<List<Payment>> getSuccessfulUserPayments(@PathVariable int userId) {
        try {
            List<Payment> payments = paymentService.findSuccessfulUserPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/failed")
    public ResponseEntity<List<Payment>> getFailedUserPayments(@PathVariable int userId) {
        try {
            List<Payment> payments = paymentService.findFailedUserPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<Payment>> getRecentUserPayments(@PathVariable int userId) {
        try {
            List<Payment> payments = paymentService.findRecentPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getUserTotalPayments(@PathVariable int userId) {
        try {
            double total = paymentService.calculateTotalUserPayments(userId);
            return ResponseEntity.ok(total);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        try {
            Payment processedPayment = paymentService.processPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(processedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/subscriptions")
    public ResponseEntity<List<UserSubscription>> getUserSubscriptions(@PathVariable int userId) {
        try {
            List<UserSubscription> subscriptions = paymentService.findUserSubscriptions(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{userId}/subscriptions/active")
    public ResponseEntity<List<UserSubscription>> getActiveUserSubscriptions(@PathVariable int userId) {
        try {
            List<UserSubscription> subscriptions = paymentService.findActiveUserSubscriptions(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<UserSubscription> createSubscription(@RequestBody UserSubscription subscription) {
        try {
            UserSubscription createdSubscription = paymentService.createUserSubscription(subscription);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
