package psu.edu.GameFlix.Services;

import org.springframework.stereotype.Service;

import psu.edu.GameFlix.Repoitories.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository thePaymentRepository) {
        paymentRepository = thePaymentRepository;
    }
}
