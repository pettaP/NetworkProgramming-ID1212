package Rates.Presentation;

import org.springframework.stereotype.Service;

@Service
public class CurrentCount {

    public Integer currentCount;

    public void setCount(Integer newCount) {
        this.currentCount = newCount;
    }
}
