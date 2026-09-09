//////////////////////////////////////////////////////////////////////////////
// ExceptionData.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.transfer;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * A serialization-friendly snapshot of a {@link Throwable}, suitable for
 * conveying failure details across a service boundary (for example, as the
 * {@code exception} carried on an {@link AbstractServiceReply}) without
 * requiring the actual exception class to be present on both sides.
 * Captures the exception's type name, an optional numeric code, its
 * message, a rendered stack trace, and, recursively, the {@link #getCause()
 * cause} chain.
 * </p>
 * <p>
 * Instances are typically produced with the {@link #of(Throwable)} factory
 * method rather than populated field-by-field.
 * </p>
 * <p>
 * <h4>Examples</h4>
 * <pre>
 * try
 * {
 *     doWork();
 * }
 * catch (Exception e)
 * {
 *     ExceptionData data = ExceptionData.of(e);
 *     reply.setException(data).setSuccess(false);
 * }
 * </pre>
 * </p>
 */
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
    setExceptionCode(Integer exceptionCode)
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
