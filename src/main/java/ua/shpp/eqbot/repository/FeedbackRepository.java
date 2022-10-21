package ua.shpp.eqbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.shpp.eqbot.model.FeedbackEntity;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
}
