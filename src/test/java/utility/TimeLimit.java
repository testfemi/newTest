package utility;

import enums.WaitTimes;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class TimeLimit {

    private Instant end; //Represents an instantaneous point in time
    private final Duration duration; //The duration for the TimeLimit

    public TimeLimit(int timeLimitInSeconds) {
        duration = Duration.ofSeconds(timeLimitInSeconds);
        reset();
    }

    public TimeLimit(Duration duration) {
        this.duration = duration;
        reset();
    }

    public TimeLimit(WaitTimes waitTime) {
        duration = waitTime.WAIT_TIME;
        reset();
    }

    public void reset(){
        end = Clock.systemDefaultZone().instant().plus(duration);
    }

    public long timeInSeconds() {
        return duration.getSeconds();
    }

    public long timeInMinutes() {
        return duration.toMinutes();
    }

    public boolean timeLeft() {
        if (end.isBefore(Clock.systemDefaultZone().instant())) {
            waitFor(1);
            return false;
        }
        return true;
    }

    public static void waitFor(int seconds) {
        waitFor(Duration.ofSeconds(seconds));
    }

    public static void waitFor(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new Error("Unable to sleep thread");
        }
    }

    public static void waitFor(WaitTimes duration) {
        waitFor(duration.WAIT_TIME);
    }

}
