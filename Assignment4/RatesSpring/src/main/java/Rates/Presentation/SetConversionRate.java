package Rates.Presentation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Transactional
@Service
public class SetConversionRate {

    private String fromCurrency;
    private String toCurrency;

    @NotNull(message = "Please specify amount")
    @PositiveOrZero(message = "Amount must be zero or greater")
    private Double newRate;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getNewRate() {
        return newRate;
    }

    public void setNewRate(Double newRate) {
        this.newRate = newRate;
    }
}
