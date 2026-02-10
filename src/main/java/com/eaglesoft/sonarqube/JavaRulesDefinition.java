package com.eaglesoft.sonarqube;

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

public class JavaRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "custom-java-rules";
    public static final String REPOSITORY_NAME = "Custom Java Rules";

    @Override
    public void define(Context context) {
        NewRepository repo = context
                .createRepository(REPOSITORY_KEY, "java")
                .setName(REPOSITORY_NAME);

        // Rule : DetectClassExtendThread
        repo.createRule("extend-thread")
                .setName("Detect extending Thread")
                .setHtmlDescription(
                        "This rule reports classes that extend <code>Thread</code>. " +
                                "In many projects, concurrency is managed using <code>Runnable</code> " +
                                "or executor-based frameworks. This rule helps teams identify " +
                                "direct <code>Thread</code> usage for review."
                )
                .setSeverity(Severity.INFO)
                .setStatus(RuleStatus.READY)
                .setType(RuleType.CODE_SMELL);

        // Rule : EmptyCatchBlockRule
        repo.createRule("empty-catch-block")
                .setName("Empty Catch Block")
                .setHtmlDescription(
                        "This rule detects empty <code>catch</code> blocks. " + "Ignoring exceptions can hide errors and make debugging difficult."
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("bug", "error-handling");

        // Rule : HardCodeCredentialRule
        repo.createRule("hardcoded-credential")
                .setName("Hard-coded credentials should not be used")
                .setHtmlDescription
                        (
                                "<p>Hard-coded credentials such as passwords, tokens, or API keys " +
                                        "pose a security risk and should not be embedded directly in source code.</p>" +
                                        "<p>Use environment variables, configuration files, or secret managers instead.</p>"
                        )
                .setSeverity(Severity.CRITICAL)
                .setStatus(RuleStatus.READY)
                .setTags("security", "credential");

        // Rule : LongMethodRule
        repo.createRule("long-method")
                .setName("Method should not be too long")
                .setHtmlDescription(
                                "<p>Long methods are difficult to read, understand, and maintain.</p>" +
                                        "<p>Consider refactoring the method by extracting smaller methods " +
                                        "to improve readability and maintainability.</p>"
                        )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("code-smell", "maintainability");

        // Rule : NoSystemOutRule
        repo.createRule("no-system-out")
                .setName("System.out and System.err should not be used")
                .setHtmlDescription(
                        "<p>Using <code>System.out</code> or <code>System.err</code> " +
                                "for logging is not recommended in production environments.</p>" +
                                "<p>Use a proper logging framework such as SLF4J or Log4j " +
                                "to enable log level management and centralized logging.</p>"
                )
                .setSeverity(Severity.MINOR)
                .setStatus(RuleStatus.READY)
                .setTags("bad-practice", "logging");

        // Rule : AvoidTuple
        repo.createRule("avoid-tuples")
                .setName("Tuple should not be used")
                .setHtmlDescription(
                        "<p>Using <code>Tuple</code>" +
                                "Tuples can reduce readability and hide domain concepts. Prefer explicit domain classes or Java </p>" +
                                "<p>Replace tuples with well-named types or records.</p>"
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("code-smell", "maintainability");

        // Rule : AvoidSpecificImport
        repo.createRule("AvoidSpecificImport")
                .setName("Tuple should not be used")
                .setHtmlDescription(
                        "<p>Using <code>Tuple</code>" +
                                "Tuples can reduce readability and hide domain concepts. Prefer explicit domain classes or Java </p>" +
                                "<p>Replace tuples with well-named types or records.</p>"
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("code-smell", "maintainability");

        // Rule : AvoidConfigurableImport
        repo.createRule("AvoidConfigurableImport")
                .setName("Tuple should not be used")
                .setHtmlDescription(
                        "<p>Using <code>Tuple</code>" +
                                "Tuples can reduce readability and hide domain concepts. Prefer explicit domain classes or Java </p>" +
                                "<p>Replace tuples with well-named types or records.</p>"
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("code-smell", "maintainability");

        // Rule : AvoidConfigurableImport
        repo.createRule("AvoidJpaTupleUsage")
                .setName("Tuple should not be used")
                .setHtmlDescription(
                        "<p>Using <code>Tuple</code>" +
                                "Tuples can reduce readability and hide domain concepts. Prefer explicit domain classes or Java </p>" +
                                "<p>Replace tuples with well-named types or records.</p>"
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("code-smell", "maintainability");

        repo.createRule("prefer-streamapi")
                .setName("Use Stream API and Lambdas instead of imperative loops")
                .setHtmlDescription(
                        "<p>Modern Java encourages functional programming patterns. Using <code>Stream " +
                                "and <code>Lambda expressions</code> makes code more declarative, easier to parallelize, and " +
                                "reduces boilerplate logic like manual iterator handling.</p>" +
                                "<p><strong>Instead of:</strong> Manual for-loops with conditional branching and temporary collections.</p>" +
                                "<p><strong>Prefer:</strong> <code>stream()</code>, <code>filter()</code>, <code>map()</code>, and <code>collect()</code>.</p>"
                )
                .setSeverity(Severity.MINOR)
                .setStatus(RuleStatus.READY)
                .setTags("modern-java", "readability", "functional-programming");

        repo.createRule("jpabestpracticescheckrule")
                .setName("Native SQL queries should be avoided")
                .setHtmlDescription(
                        "<p>Native SQL queries make the application dependent on a specific database vendor and bypass JPA's type-checking benefits.</p>" +
                                "<p>Prefer using <strong>JPQL</strong> or the <strong>Criteria API</strong> to maintain database portability and allow JPA to manage entity lifecycles effectively.</p>" +
                                "<h3>Noncompliant Code Example</h3>" +
                                "<pre>Query query = entityManager.createNativeQuery(\"SELECT * FROM users WHERE status = 'ACTIVE'\", User.class);</pre>" +
                                "<h3>Compliant Solution</h3>" +
                                "<pre>TypedQuery&lt;User&gt; query = entityManager.createQuery(\"SELECT u FROM User u WHERE u.status = :status\", User.class);\n" +
                                "query.setParameter(\"status\", Status.ACTIVE);</pre>"
                )
                .setSeverity(Severity.MAJOR)
                .setStatus(RuleStatus.READY)
                .setTags("sql", "portability", "maintainability");
        repo.done();
    }
}
