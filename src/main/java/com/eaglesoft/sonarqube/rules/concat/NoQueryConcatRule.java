package com.eaglesoft.sonarqube.rules.concat;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.semantic.MethodMatchers;

import java.util.Collections;
import java.util.List;

@Rule(key = "noconcatquerycheckrule")
public class NoQueryConcatRule extends IssuableSubscriptionVisitor {

    private static final MethodMatchers QUERY_METHODS = MethodMatchers.create()
            .ofTypes("javax.persistence.EntityManager", "jakarta.persistence.EntityManager")
            .names("createQuery", "createNativeQuery")
            .withAnyParameters()
            .build();

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        MethodInvocationTree mit = (MethodInvocationTree) tree;

        if (QUERY_METHODS.matches(mit) && !mit.arguments().isEmpty()) {
            ExpressionTree queryArg = mit.arguments().get(0);

            if (isStringConcatenation(queryArg)) {
                reportIssue(queryArg, "Use bind parameters instead of String concatenation to build this query.");
            }
        }
    }

    private boolean isStringConcatenation(ExpressionTree expression) {
        // Use BinaryExpressionTree to inspect the PLUS (+) operator
        if (expression.is(Tree.Kind.PLUS)) {
            BinaryExpressionTree bet = (BinaryExpressionTree) expression;
            return bet.symbolType().is("java.lang.String");
        }
        return false;
    }
}