package com.codetropics.java.agent;

import com.codetropics.java.asm.timemachine.TimeMachineAgentDelegate;

import java.lang.instrument.Instrumentation;
import java.util.regex.Pattern;

public class TimeMachineAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        if (agentArgs == null || agentArgs.trim().equalsIgnoreCase("help") || agentArgs.trim().equalsIgnoreCase("-h")) {
            printHelp();
            return;
        }

        String config = agentArgs; // Simplest case
        Pattern[] includePatterns = new Pattern[] { Pattern.compile(".*") }; // All classes
        Pattern[] excludePatterns = new Pattern[0]; // None

        TimeMachineAgentDelegate.premain(
                includePatterns,
                excludePatterns,
                config,
                inst
        );
    }

    public static void printHelp() {
        System.out.println("\n=== TimeMachine Java Agent Usage ===\n");
        System.out.println("Shift the system time for your JVM process without touching the system clock.\n");
        System.out.println("Usage:");
        System.out.println("  -javaagent:timemachine-delegate.jar=<time-shift>");
        System.out.println();
        System.out.println("Time shift can be relative or absolute:");
        System.out.println("  Examples:");
        System.out.println("    -1d         # minus 1 day");
        System.out.println("    +2h30m      # plus 2 hours 30 minutes");
        System.out.println("    -1w2d       # minus 1 week and 2 days");
        System.out.println("    2025-06-10T14:00:00   # absolute time (YYYY-MM-DDTHH:MM:SS)");
        System.out.println();
        System.out.println("Supported units: y (years), mo (months), w (weeks), d (days), h (hours), m (minutes), s (seconds)");
        System.out.println();
        System.out.println("Show this help:");
        System.out.println("  -javaagent:timemachine-delegate.jar=help\n");
        System.out.println("Example:");
        System.out.println("  java -javaagent:timemachine-delegate.jar=+3h -jar yourapp.jar\n");
    }
}
