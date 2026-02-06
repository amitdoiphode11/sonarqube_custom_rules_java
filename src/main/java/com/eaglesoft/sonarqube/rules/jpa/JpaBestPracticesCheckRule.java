package com.eaglesoft.sonarqube.rules.jpa;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "jpabestpracticescheckrule")
public class JpaBestPracticesCheckRule extends IssuableSubscriptionVisitor {

  // Matcher for createNativeQuery(...)
  private static final MethodMatchers NATIVE_QUERY_MATCHER = MethodMatchers.create()
          .ofTypes("javax.persistence.EntityManager", "jakarta.persistence.EntityManager")
          .names("createNativeQuery")
          .withAnyParameters()
          .build();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    // We are looking for method calls (Ianvocations)
    return Collections.singletonList(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree mit = (MethodInvocationTree) tree;

    // 1. Check for Native SQL Usage
    if (NATIVE_QUERY_MATCHER.matches(mit)) {
      reportIssue(mit.methodSelect(), "Avoid using native SQL queries. Prefer JPQL or Criteria API for better maintainability.");
    }
  }
}