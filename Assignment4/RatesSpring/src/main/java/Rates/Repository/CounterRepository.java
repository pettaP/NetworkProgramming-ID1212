package Rates.Repository;

import Rates.Domain.Counter;
import Rates.Domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterRepository extends JpaRepository<Counter, Integer> {

    Counter findFirstdById(Integer counter_id);
}
