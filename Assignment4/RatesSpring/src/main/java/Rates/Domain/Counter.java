package Rates.Domain;

import javax.persistence.*;

@Entity
@Table(name = "COUNTER")
public class Counter implements CounterDTO {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "COUNT")
    private Integer counter;

    @Override
    public Integer getCounter() {
        return this.counter;
    }

    public void incrementCount() {
        this.counter = this.counter+1;
    }

    protected Counter(){}
}
