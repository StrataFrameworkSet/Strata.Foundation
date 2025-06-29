//////////////////////////////////////////////////////////////////////////////
// TestExecutionReporter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.testrunner;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public
class TestExecutionReporter
    implements TestExecutionListener
{
    @Override
    public void
    executionSkipped(TestIdentifier identifier,String reason)
    {
        System.out.println(identifier.getDisplayName() + " skipped: " + reason);
    }

    @Override
    public void
    executionStarted(TestIdentifier identifier)
    {
        System.out.println(identifier.getDisplayName() + " started:");
    }

    @Override
    public void
    executionFinished(TestIdentifier identifier,TestExecutionResult result)
    {
        System.out.println(identifier.getDisplayName() + " finished: " + result.toString());
    }
}

//////////////////////////////////////////////////////////////////////////////
