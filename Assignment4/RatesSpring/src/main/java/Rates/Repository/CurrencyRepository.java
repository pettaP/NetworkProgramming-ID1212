package Rates.Repository;

import Rates.Domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {

    Currency findByCountry(String country_code);
}
