//////////////////////////////////////////////////////////////////////////////
// JunitTestRunner.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.testrunner;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

/**
 * <p>
 * A command-line entry point that discovers and runs JUnit Platform tests
 * for a given package, filtered by one or more tags. It builds a
 * {@link org.junit.platform.launcher.LauncherDiscoveryRequest} from the
 * supplied {@code --package} and {@code --tags} arguments, registers a
 * {@link org.junit.platform.launcher.listeners.SummaryGeneratingListener}
 * together with a {@link TestExecutionReporter} on a
 * {@link org.junit.platform.launcher.Launcher}, executes the discovered
 * tests, and prints a summary (and any failures) to standard output. The
 * process exits with status {@code 1} if any test fails, or if the required
 * command-line arguments are missing, and {@code 0} otherwise.
 * </p>
 * <br/>
 * <b>Examples</b><br/>
 * <pre>
 * java -jar strata-foundation-core.jar \
 *     --package=strata.foundation.core \
 *     --tags=unit,fast
 * </pre>
 */
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
                "Usage: java -jar <jarfile> --tags=<tagName> --package=<packageName>");
            System.exit(1);
        }

        runTests(tag,packageName);
    }

    private static String
    parseTagArgument(String[] args)
    {
        for (String arg: args)
        {
            if (arg.startsWith("--tags="))
            {
                return arg.substring(7);
            }
        }
        return null;
    }

    private static String
    parsePackageArgument(String[] args)
    {
        for (String arg: args)
        {
            if (arg.startsWith("--package="))
            {
                return arg.substring(10);
            }
        }
        return null;
    }

    private static void
    runTests(String tags,String packageName)
    {
        LauncherDiscoveryRequest request =
            LauncherDiscoveryRequestBuilder
                .request()
                .selectors(selectPackage(packageName))
                .filters(includeTags(tags.split(",")))
                .build();
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        TestExecutionSummary summary = null;
        PrintWriter out = new PrintWriter(System.out);

        launcher.registerTestExecutionListeners(listener,new TestExecutionReporter());
        printRequestDetails(request,tags,packageName);
        launcher.execute(request);

        summary = listener.getSummary();
        summary.printTo(out);

        if (summary.getTestsFailedCount() > 0)
        {
            summary.printFailuresTo(out);
            System.exit(1);
        }

        out.flush();
        System.exit(0);
    }

    private static void
    printRequestDetails(LauncherDiscoveryRequest request,String tags,String packageName)
    {
        System.out.println("\n=== LauncherDiscoveryRequest Configuration ===");
        System.out.println("Root package (recursive): " + packageName);
        System.out.println("Tags filter: " + tags);

        // Print selectors
        System.out.println("\nSelectors:");
        request.getSelectorsByType(org.junit.platform.engine.discovery.PackageSelector.class)
            .forEach(selector -> System.out.println("  - Package: " + selector.getPackageName()));
        request.getSelectorsByType(org.junit.platform.engine.discovery.ClassSelector.class)
            .forEach(selector -> System.out.println("  - Class: " + selector.getClassName()));
        request.getSelectorsByType(org.junit.platform.engine.discovery.MethodSelector.class)
            .forEach(selector -> System.out.println("  - Method: " + selector.getClassName() + "#" + selector.getMethodName()));

        // Print filters
        System.out.println("\nEngine filters:");
        request.getEngineFilters().forEach(filter -> System.out.println("  - " + filter));

        System.out.println("\nPost-discovery filters:");
        request.getPostDiscoveryFilters().forEach(filter -> System.out.println("  - " + filter));

        System.out.println("============================================\n");
    }
}

/// ///////////////////////////////////////////////////////////////////////////
