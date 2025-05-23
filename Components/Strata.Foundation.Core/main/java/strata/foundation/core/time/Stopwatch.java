/// ///////////////////////////////////////////////////////////////////////////
// Stopwatch.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.time;

import java.time.Duration;
import java.time.Instant;

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
        Duration elapsed = Duration.between(startTime,stopTime);

        return elapsed.compareTo(duration) >= 0;
    }

    public boolean
    isRunning()
    {
        return this.running;
    }
}

//////////////////////////////////////////////////////////////////////////////
