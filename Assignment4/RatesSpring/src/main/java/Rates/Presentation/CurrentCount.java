package Rates.Presentation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CurrentCount {

    public Integer currentCount;

    public void setCount(Integer newCount) {
        this.currentCount = newCount;
    }
}
