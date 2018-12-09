package Rates.Domain;

import javax.persistence.*;

@Entity
@Table(name = "CURRENCY")
public class Currency implements CurrencyDTO {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "COUNTRY")
    private String country;

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getCode() {
        return this.country;
    }

    protected Currency() {}

}
