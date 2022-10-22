package ua.shpp.eqbot.model;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedback;
    private Long telegramId;
    private int rate;
    private String textFeedback;

    public Long getIdFeedback() {
        return idFeedback;
    }

    public FeedbackEntity setIdFeedback(Long idFeedback) {
        this.idFeedback = idFeedback;
        return this;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public FeedbackEntity setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    public int getRate() {
        return rate;
    }

    public FeedbackEntity setRate(int rate) {
        this.rate = rate;
        return this;
    }

    public String getTextFeedback() {
        return textFeedback;
    }

    public FeedbackEntity setTextFeedback(String textFeedback) {
        this.textFeedback = textFeedback;
        return this;
    }

    @Override
    public String toString() {
        return "FeedbackEntity{"
                + "idFeedback=" + idFeedback
                + ", telegramId=" + telegramId
                + ", rate=" + rate
                + ", textFeedback='" + textFeedback + '\'' + '}';
    }
}
