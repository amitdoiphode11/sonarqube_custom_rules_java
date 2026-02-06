package com.eaglesoft.sonarqube.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.CatchTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "empty-catch-block")
public class EmptyCatchBlockRule extends IssuableSubscriptionVisitor {
    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CATCH);
    }

    @Override
    public void visitNode(Tree tree) {
        CatchTree catchTree = (CatchTree) tree;

        if (catchTree.block().body().isEmpty()) {
            reportIssue(
                    catchTree.catchKeyword(),
                    "Empty catch block detected. Exception should not be silently ignored."
            );
        }
    }

}
