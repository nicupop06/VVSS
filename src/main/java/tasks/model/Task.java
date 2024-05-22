package tasks.model;

import org.apache.log4j.Logger;
import tasks.services.TaskIO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Task implements Serializable {
    private static final Logger log = Logger.getLogger(Task.class.getName());
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String title;
    private String description;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;

    public Task(String title, Date time) {
        if (time.getTime() < 0) {
            log.error("time below bound");
            throw new IllegalArgumentException("Time cannot be negative");
        }
        this.title = title;
        this.time = time;
        this.start = time;
        this.end = time;
    }

    public Task(Task task){
        this.title = task.title;
        this.time = task.time;
        this.start = task.start;
        this.end = task.end;
    }

    public Task(String title, Date start, Date end, int interval) {
        if (start.getTime() < 0 || end.getTime() < 0) {
            log.error("time below bound");
            throw new IllegalArgumentException("Time cannot be negative");
        }
        if (interval < 1) {
            log.error("interval < than 1");
            throw new IllegalArgumentException("interval should me > 1");
        }
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.time = start;
    }

    public static SimpleDateFormat getDateFormat() {
        return sdf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
        this.start = time;
        this.end = time;
        this.interval = 0;
    }

    public Date getStartTime() {
        return start;
    }

    public Date getEndTime() {
        return end;
    }

    public int getRepeatInterval() {
        return interval > 0 ? interval : 0;
    }

    public void setTime(Date start, Date end, int interval) {
        this.time = start;
        this.start = start;
        this.end = end;
        this.interval = interval;

    }

    public boolean isRepeated() {
        return this.interval != 0;

    }
    public Date nextTimeAfter(Date current) {
        if (isTimeAfterOrEqualToEnd(current)) return null;
        if (isSingleEvent()) return nextTimeForSingleEvent(current);
        if (isRepeatedEvent()) return nextTimeForRepeatedEvent(current);
        return null;
    }

    private boolean isTimeAfterOrEqualToEnd(Date current) {
        return current.after(end) || current.equals(end);
    }

    private boolean isSingleEvent() {
        return !isRepeated() && isActive();
    }

    private boolean isRepeatedEvent() {
        return isRepeated() && isActive();
    }

    private Date nextTimeForSingleEvent(Date current) {
        if (current.before(time)) {
            return time;
        }
        return null;
    }

    private Date nextTimeForRepeatedEvent(Date current) {
        if (current.before(start)) {
            return start;
        }
        if (current.equals(start)) {
            return new Date(start.getTime() + interval * 1000);
        }
        return calculateNextTimeForRepeatedEvent(current);
    }

    private Date calculateNextTimeForRepeatedEvent(Date current) {
        Date timeAfter = new Date(start.getTime() + interval * 1000);
        for (long i = start.getTime() + interval * 1000; i <= end.getTime(); i += interval * 1000) {
            if (current.before(timeAfter) || current.equals(timeAfter)) {
                return timeAfter;
            }
            timeAfter = new Date(timeAfter.getTime() + interval * 1000);
        }
        return null;
    }

    //duplicate methods for TableView which sets column
    // value by single method and doesn't allow passing parameters
    public String getFormattedDateStart() {
        return sdf.format(start);
    }

    public String getFormattedDateEnd() {
        return sdf.format(end);
    }

    public String getFormattedRepeated() {
        if (isRepeated()) {
            String formattedInterval = TaskIO.getFormattedInterval(interval);
            return "Every " + formattedInterval;
        } else {
            return "No";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!time.equals(task.time)) return false;
        if (!start.equals(task.start)) return false;
        if (!end.equals(task.end)) return false;
        if (interval != task.interval) return false;
        if (active != task.active) return false;
        return title.equals(task.title);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + interval;
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", active=" + active +
                '}';
    }
}

