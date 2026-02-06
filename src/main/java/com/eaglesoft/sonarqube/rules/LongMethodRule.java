package com.eaglesoft.sonarqube.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;
@Rule(key = "long-method")
public class LongMethodRule extends IssuableSubscriptionVisitor {

    private static final int MAX_STATEMENTS = 30;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.METHOD);
    }

    @Override
    public void visitNode(Tree tree){
        MethodTree methodTree = (MethodTree) tree;

        BlockTree body = methodTree.block();
        if (body == null){
            return;
        }

        int statementCount = body.body().size();

        if (statementCount > MAX_STATEMENTS) {
            reportIssue(
                    methodTree.simpleName(),
                    String.format(
                            "Method has %d statements, which exceeds the maximum allowed (%d). Consider refactoring.",
                            statementCount,
                            MAX_STATEMENTS
                    )
            );
        }
    }
}
