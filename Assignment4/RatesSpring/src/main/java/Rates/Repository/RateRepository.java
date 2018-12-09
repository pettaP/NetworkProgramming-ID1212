package Rates.Repository;

import Rates.Domain.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Integer>{

    Rate findByStartAndEnd(int from, int to);

}
