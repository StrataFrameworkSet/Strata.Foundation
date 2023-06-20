//////////////////////////////////////////////////////////////////////////////
// ExceptionData.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.io.PrintWriter;
import java.io.StringWriter;

public
class ExceptionData
{
    private String        itsExceptionType;
    private Integer       itsExceptionCode;
    private String        itsExceptionMessage;
    private String        itsStackTrace;
    private ExceptionData itsCause;

    public
    ExceptionData()
    {
        itsExceptionType = null;
        itsExceptionCode = null;
        itsExceptionMessage = null;
        itsStackTrace = null;
        itsCause = null;
    }

    public ExceptionData
    setExceptionType(String exceptionType)
    {
        itsExceptionType = exceptionType;
        return this;
    }

    public ExceptionData
    setExceptionCode(int exceptionCode)
    {
        itsExceptionCode = exceptionCode;
        return this;
    }

    public ExceptionData
    setExceptionMessage(String exceptionMessage)
    {
        itsExceptionMessage = exceptionMessage;
        return this;
    }

    public ExceptionData
    setStackTrace(String stackTrace)
    {
        itsStackTrace = stackTrace;
        return this;
    }

    public ExceptionData
    setCause(ExceptionData cause)
    {
        itsCause = cause;
        return this;
    }

    public String
    getExceptionType() { return itsExceptionType; }

    public Integer
    getExceptionCode() { return itsExceptionCode; }

    public String
    getExceptionMessage() { return itsExceptionMessage; }

    public String
    getStackTrace() { return itsStackTrace; }

    public ExceptionData
    getCause() { return itsCause; }

    public boolean
    hasCause() { return itsCause != null; }

    public static ExceptionData
    of(Throwable source)
    {
        if (source != null)
        {
            StringWriter  writer = new StringWriter();
            ExceptionData dest   = new ExceptionData();

            source.printStackTrace(new PrintWriter(writer));

            dest
                .setExceptionType(source.getClass().getCanonicalName())
                .setExceptionMessage(source.getMessage())
                .setStackTrace(writer.toString())
                .setCause(of(source.getCause()));

            return dest;
        }

        return null;
    }

}

//////////////////////////////////////////////////////////////////////////////
