/// ///////////////////////////////////////////////////////////////////////////
// JunitTestRunner.java
/// ///////////////////////////////////////////////////////////////////////////

package strata.foundation.core.testrunner;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

import java.io.PrintWriter;

public class JunitTestRunner
{
    public static void
    main(String[] args)
    {
        String tag = parseTagArgument(args);
        String packageName = parsePackageArgument(args);

        if (tag == null || packageName == null)
        {
            System.err.println(
                "Usage: java -jar <jarfile> --tag=<tagName> --package=<packageName>");
            System.exit(1);
        }

        runTests(tag,packageName);
    }

    private static String
    parseTagArgument(String[] args)
    {
        for (String arg: args)
        {
            if (arg.startsWith("-tag="))
            {
                return arg.substring(5);
            }
        }
        return null;
    }

    private static String
    parsePackageArgument(String[] args)
    {
        for (String arg: args)
        {
            if (arg.startsWith("-package="))
            {
                return arg.substring(5);
            }
        }
        return null;
    }

    private static void
    runTests(String tag, String packageName)
    {
        LauncherDiscoveryRequest request =
            LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectPackage(packageName))
                .filters(includeTags(tag))
                .build();
        Launcher                  launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        TestExecutionSummary      summary = null;
        PrintWriter               out = new PrintWriter(System.out);

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        summary = listener.getSummary();
        summary.printTo(out);
        out.flush();

        if (summary.getTestsFailedCount() > 0)
            System.exit(1);
    }
}

//////////////////////////////////////////////////////////////////////////////
