package com.vineeth.security;

/**
 * Intentionally noncompliant: hard-coded credential to trigger Sonar rule S2068.
 */
public class DebugLeak {
    // Noncompliant (S2068): "password" is a credential keyword
    private static final String DB_PASSWORD = "P@ssw0rd!";

    public static String leak() {
        return DB_PASSWORD;
    }
}
