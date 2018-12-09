package Rates.Application;

import Rates.Domain.Counter;
import Rates.Domain.Currency;
import Rates.Domain.Rate;
import Rates.Repository.CounterRepository;
import Rates.Repository.CurrencyRepository;
import Rates.Repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class RatesService {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private CounterRepository counterRepository;

    private Currency fromCurr;
    private Currency toCurr;
    private Rate rate;

    public double getRate(String fromCode, String toCode) {
        setCurrencyId(fromCode, toCode);
        getConversionRate();
        return this.rate.getRate();

    }

    private void setCurrencyId(String fromCode, String toCode) {
        this.fromCurr = currencyRepository.findByCountry(fromCode);
        this.toCurr = currencyRepository.findByCountry(toCode);
    }

    private void getConversionRate() {
        this.rate = rateRepository.findByStartAndEnd(fromCurr.getId(), toCurr.getId());
    }

    public void setRate(String fromCode, String toCode, Double newRate) {
        setCurrencyId(fromCode, toCode);
        this.rate = rateRepository.findByStartAndEnd(fromCurr.getId(), toCurr.getId());
        this.rate.setRate(newRate);
    }

    public Integer getNumberOfConversions() {
        return counterRepository.findFirstdById(1).getCounter();
    }

    public void incrementNumberOfConversions() {
        counterRepository.findFirstdById(1).incrementCount();
    }

}
