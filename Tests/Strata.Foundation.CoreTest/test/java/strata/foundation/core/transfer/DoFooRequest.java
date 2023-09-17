//////////////////////////////////////////////////////////////////////////////
// DoFooRequest.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

public
class DoFooRequest
    extends AbstractServiceRequest
{
    private String foo;

    public
    DoFooRequest()
    {
        super();
        foo = null;
    }

    public DoFooRequest
    setFoo(String f)
    {
        foo = f;
        return this;
    }

    public String
    getFoo() { return foo; }

    @Override
    public boolean
    equals(Object other)
    {
        if (other instanceof DoFooRequest o)
            return
                getRequestId().equals(o.getRequestId()) &&
                getTimestamp().equals(o.getTimestamp()) &&
                getFoo().equals(o.getFoo());

        return false;
    }
}

//////////////////////////////////////////////////////////////////////////////
