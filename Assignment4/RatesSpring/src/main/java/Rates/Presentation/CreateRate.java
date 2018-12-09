package Rates.Presentation;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
