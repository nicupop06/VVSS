package tasks.services;

import java.util.Date;

public class TaskValidator {
    public void validate(String title, Date startDate, Date endDate, int interval) {

        if(title == null) throw new IllegalArgumentException("Title cannot be null");
        if(interval < 1) throw new IllegalArgumentException("Interval can not be lower than 1");
        if(startDate.after(endDate)) throw new IllegalArgumentException("Start date should be before end");
        if(title.equals("")) throw new IllegalArgumentException("Title must not be empty");
    }
}
