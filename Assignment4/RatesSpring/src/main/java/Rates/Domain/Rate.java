package Rates.Domain;

import javax.persistence.*;

@Entity
@Table (name = "RATES")
public class Rate implements RateDTO {

    @Id
    @Column(name = "ID")
    private Integer country_id;

    @Column(name = "START")
    private int start;

    @Column(name = "END")
    private Integer end;

    @Column(name = "RATE")
    private Double rate;


    public void setRate(Double newRate) {
        this.rate = newRate;
    }
    @Override
    public double getRate() {
        return this.rate;
    }

    protected Rate() {}
}
