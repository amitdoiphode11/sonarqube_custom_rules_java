package com.eaglesoft.sonarqube.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Arrays;
import java.util.List;

@Rule(key = "no-system-out")
public class NoSystemOutRule extends IssuableSubscriptionVisitor {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        MethodInvocationTree methodInvocationTree = (MethodInvocationTree) tree;

        ExpressionTree select = methodInvocationTree.methodSelect();

        if (!(select instanceof MemberSelectExpressionTree)) {
            return;
        }

        MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) select;

        String methodName = memberSelect.identifier().name();

        // println / print
        if (!("println".equals(methodName) || "print".equals(methodName))) {
            return;
        }

        // System.out / System.err
        ExpressionTree expression = memberSelect.expression();

       if (expression instanceof MemberSelectExpressionTree) {
           MemberSelectExpressionTree owner = (MemberSelectExpressionTree) expression;

           boolean isSystem =
                   "System".equals(owner.expression().toString()) &&
                           ("out".equals(owner.identifier().name()) || "err".equals(owner.identifier().name()));

           if (isSystem) {
               reportIssue(
                       memberSelect,
                       "Avoid using System.out/System.err. Use a logging framework instead."
               );
           }
       }
    }

}
