//////////////////////////////////////////////////////////////////////////////
// FooEvent.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.chronicle.event;

import net.openhft.chronicle.wire.Marshallable;

public
class FooEvent
    implements Marshallable
{
    private String x;
    private Integer y;

    public FooEvent()
    {
        this(null,null);
    }

    public
    FooEvent(String a,Integer b)
    {
        x = a;
        y = b;
    }

    @Override
    public int
    hashCode()
    {
        return x.hashCode() + y.hashCode();
    }

    @Override
    public boolean
    equals(Object other)
    {
        return
            other instanceof FooEvent event
                ? x.equals(event.x) && y.equals(event.y)
                : false;
    }

    public FooEvent
    setX(String value)
    {
        x = value;
        return this;
    }

    public FooEvent
    setY(Integer value)
    {
        y = value;
        return this;
    }

    public String
    getX() { return x; }

    public Integer
    getY() { return y; }
}

//////////////////////////////////////////////////////////////////////////////
