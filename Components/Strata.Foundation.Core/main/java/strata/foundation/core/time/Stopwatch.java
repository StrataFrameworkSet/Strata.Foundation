//////////////////////////////////////////////////////////////////////////////
// Stopwatch.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Duration;
import java.time.Instant;

/**
 * <p>
 * A simple, mutable timer for measuring elapsed wall-clock time using
 * {@link java.time.Instant} and {@link java.time.Duration}. A stopwatch is
 * started with {@link #start()} and stopped with {@link #stop()}; calling
 * {@link #getDuration()} implicitly stops a running stopwatch before
 * computing the elapsed time between the start and stop instants.
 * {@link #restart()} stops (if running) and immediately starts a fresh
 * timing interval, while {@link #hasElapsed(Duration)} checks whether at
 * least the given duration has passed since the stopwatch was started,
 * using the current time in place of a missing start or stop instant. This
 * class is not thread-safe; a single instance should not be shared across
 * threads without external synchronization.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * Stopwatch stopwatch = new Stopwatch().start();
 * // ... perform work ...
 * Duration elapsed = stopwatch.stop().getDuration();
 * System.out.println("Elapsed: " + elapsed);
 *
 * Stopwatch timeout = new Stopwatch().start();
 * while (!timeout.hasElapsed(Duration.ofSeconds(30)))
 * {
 *     // poll for completion
 * }
 * </pre>
 * </p>
 */
public
class Stopwatch
{
    private Instant startTime;
    private Instant stopTime;
    private boolean running;

    public
    Stopwatch()
    {
        this.startTime = null;
        this.stopTime  = null;
        this.running   = false;
    }

    public Stopwatch
    start()
    {
        if (!isRunning())
        {
            this.startTime = Instant.now();
            this.stopTime  = null;
            this.running = true;
        }
        return this;
    }

    public Stopwatch
    stop()
    {
        if (isRunning())
        {
            this.stopTime = Instant.now();
            this.running  = false;
        }
        return this;
    }

    public Stopwatch
    restart()
    {
        if (isRunning())
            this.stop();

        start();
        return this;
    }

    public Duration
    getDuration()
    {
        if (isRunning())
            this.stop();

        if (this.startTime == null || this.stopTime == null)
            return Duration.ZERO;

        return Duration.between(this.startTime,this.stopTime);
    }

    public boolean
    hasElapsed(Duration duration)
    {
        Instant startingTime =
            this.startTime != null
                ? this.startTime
                : Instant.now();
        Instant stoppingTime =
            this.stopTime != null
                ? this.stopTime
                : Instant.now();
        Duration elapsed = Duration.between(startingTime,stoppingTime);

        return elapsed.compareTo(duration) >= 0;
    }

    public boolean
    isRunning()
    {
        return this.running;
    }
}

//////////////////////////////////////////////////////////////////////////////
