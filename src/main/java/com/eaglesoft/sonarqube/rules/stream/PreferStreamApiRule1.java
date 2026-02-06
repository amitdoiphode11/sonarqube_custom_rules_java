package com.eaglesoft.sonarqube.rules.stream;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.ForEachStatement;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "prefer-streamapi")
public class PreferStreamApiRule1 extends IssuableSubscriptionVisitor {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        // We only want to look at "for-each" loops
        return Collections.singletonList(Tree.Kind.FOR_EACH_STATEMENT);
    }

    @Override
    public void visitNode(Tree tree) {
        ForEachStatement forEach = (ForEachStatement) tree;
        StatementTree body = forEach.statement();

        // Check if the loop body contains exactly one 'if' statement
        if (body.is(Tree.Kind.BLOCK)) {
            BlockTree block = (BlockTree) body;
            if (block.body().size() == 1 && block.body().get(0).is(Tree.Kind.IF_STATEMENT)) {
                reportIssue(forEach.forKeyword(), "This loop can be simplified using Stream API .filter().");
            }
        } else if (body.is(Tree.Kind.IF_STATEMENT)) {
            // Single-line for-each without braces: for(X x : list) if(cond) ...
            reportIssue(forEach.forKeyword(), "This loop can be simplified using Stream API .filter().");
        }
    }
}
 