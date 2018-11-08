package com.ronan;

import com.flexionmobile.codingchallenge.integration.IntegrationTestRunner;

public class Main {

    public static void main(String[] args) {

        IntegrationTestRunner integrationTestRunner = new IntegrationTestRunner();
        integrationTestRunner.runTests(new IntegrationImpl());

    }
}
