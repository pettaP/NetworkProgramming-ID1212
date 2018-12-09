package Rates.Presentation;

import Rates.Application.RatesService;
import Rates.Domain.Counter;
import Rates.Domain.Currency;
import Rates.Repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@Scope("session")
public class ConversionController {

    static final String DEFAULT_PAGE_URL = "/";
    static final String SELECT_RATES_PAGE_URL = "conversionForm";
    static final String MAKE_CONVERISON_URL = "create-conversion";
    static final String ADMIN_URL = "admin";
    static final String ADMIN_RATES_URL = "setRates";
    static final String SET_RATES_PAGE_URL = "set-conversionrate";

    @Autowired
    RatesService service;

    private Integer numberOfConversions;

    @GetMapping(DEFAULT_PAGE_URL)
    public String showDefaultView(CreateRate createRate) {
        createRate.setAmount(0.0);
        return "redirect:" + SELECT_RATES_PAGE_URL;
    }

    @GetMapping("/" + SELECT_RATES_PAGE_URL)
    public String showRatesSelectionView(CreateConversion createConversionForm, CreateRate createRate) {
        return SELECT_RATES_PAGE_URL;
    }

    @PostMapping("/" + MAKE_CONVERISON_URL)
    public String makeConversion(@Valid CreateConversion createConversion, BindingResult bindingResult, CreateRate createRate) {
        if(bindingResult.hasErrors()) {
            createRate.setAmount(0.0);
            return SELECT_RATES_PAGE_URL;
        } else if (createConversion.getFromCurrency().equalsIgnoreCase(createConversion.getToCurrency())) {
            createRate.setAmount(createConversion.getAmount());
            return SELECT_RATES_PAGE_URL;
        }
        service.incrementNumberOfConversions();
        createRate.makeCoversion(createConversion.getAmount(), service.getRate(createConversion.getFromCurrency(), createConversion.getToCurrency()));

        return SELECT_RATES_PAGE_URL;
    }

    @GetMapping("/" + ADMIN_URL)
    public String showAdminView(CurrentCount currentCount) {
        currentCount.setCount(service.getNumberOfConversions());
        return "redirect:" + ADMIN_RATES_URL;
    }

    @GetMapping("/" + ADMIN_RATES_URL)
    public String showRatesView(SetConversionRate setConversionRate, CurrentCount currentCount) {
        currentCount.setCount(service.getNumberOfConversions());
        return ADMIN_RATES_URL;
    }

    @PostMapping ("/" + SET_RATES_PAGE_URL)
    public String setNewRate(@Valid SetConversionRate setConversionRate, BindingResult bindingResult, CurrentCount currentCount) {
        currentCount.setCount(service.getNumberOfConversions());
        if(bindingResult.hasErrors()) {
            setConversionRate.setNewRate(0.0);
            return ADMIN_RATES_URL;
        } else if (setConversionRate.getFromCurrency().equalsIgnoreCase(setConversionRate.getToCurrency())) {
            setConversionRate.setNewRate(1.0);
            return ADMIN_RATES_URL;
        } else {
            service.setRate(setConversionRate.getFromCurrency(), setConversionRate.getToCurrency(), setConversionRate.getNewRate());
        }

        return ADMIN_RATES_URL;
    }
}
