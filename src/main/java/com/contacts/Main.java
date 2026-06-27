package com.contacts;

import com.contacts.cli.CliApplication;

/** Application entry point for the contacts manager. */
public final class Main {

    private Main() {
        // Entry-point utility class.
    }

    /**
     * Main method for the contacts CLI.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int exitCode = new CliApplication(System.out, System.err).run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }
}
