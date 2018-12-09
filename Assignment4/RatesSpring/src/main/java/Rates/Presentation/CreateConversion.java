package Rates.Presentation;

import javax.validation.constraints.*;

public class CreateConversion {

    @NotBlank(message = "Fill in currency. SEK, GDP, USD, EUR")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "Only letters are allowed")
    @Size(min = 3, max = 3, message = "Currency must be three letters")
    private String fromCurrency;

    @NotBlank(message = "Fill in currency. SEK, GDP, USD, EUR")
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "Only letters are allowed")
    @Size(min = 3, max = 3, message = "Currency must be three letters")
    private String toCurrency;

    @NotNull(message = "Please specify amount")
    @PositiveOrZero(message = "Amount must be zero or greater")
    private Double amount;

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public Double getAmount() {
        return amount;
    }
}
