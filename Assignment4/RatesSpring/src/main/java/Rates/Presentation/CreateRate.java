package Rates.Presentation;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CreateRate {

    public Double amount;

    public void makeCoversion(Double amount, Double rate) {
        this.amount = amount * rate;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
