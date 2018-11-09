package com.ronan;

import com.flexionmobile.codingchallenge.integration.IntegrationTestRunner;
import com.ronan.Integration.IntegrationImpl;

public class Main {

    public static void main(String[] args) {

        IntegrationTestRunner integrationTestRunner = new IntegrationTestRunner();
        integrationTestRunner.runTests(new IntegrationImpl());

    }
}
