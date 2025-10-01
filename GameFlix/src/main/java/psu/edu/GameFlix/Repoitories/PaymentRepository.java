package psu.edu.GameFlix.Repoitories;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
    