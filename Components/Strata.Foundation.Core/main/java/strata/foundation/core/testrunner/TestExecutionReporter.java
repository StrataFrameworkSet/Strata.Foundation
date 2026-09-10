//////////////////////////////////////////////////////////////////////////////
// TestExecutionReporter.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.testrunner;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

/**
 * <p>
 * A simple {@link TestExecutionListener} that writes test lifecycle events
 * (skipped, started, and finished) to standard output as they are reported
 * by the JUnit Platform {@link org.junit.platform.launcher.Launcher} during
 * a test run. Each line identifies the affected test or container by its
 * {@link TestIdentifier#getDisplayName()} together with the outcome
 * reported for that event, providing a lightweight, real-time console trace
 * that complements the end-of-run summary produced separately by listeners
 * such as {@link org.junit.platform.launcher.listeners.SummaryGeneratingListener}.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * Launcher launcher = LauncherFactory.create();
 * launcher.registerTestExecutionListeners(new TestExecutionReporter());
 * launcher.execute(request);
 * </pre>
 */
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
