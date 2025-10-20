package psu.edu.GameFlix.Repoitories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import psu.edu.GameFlix.Models.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    List<UserSubscription> findByUserId(int userId);

    List<UserSubscription> findByPlanId(int planId);

    List<UserSubscription> findByStatus(String status);
}
