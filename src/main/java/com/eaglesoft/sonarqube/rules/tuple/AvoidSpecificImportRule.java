package com.eaglesoft.sonarqube.rules.tuple;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

@Rule(key = "AvoidSpecificImport")
public class AvoidSpecificImportRule extends IssuableSubscriptionVisitor {

    private static final String FORBIDDEN_IMPORT = "javax.persistence.Tuple";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(Tree.Kind.IMPORT);
    }

    @Override
    public void visitNode(Tree tree) {
        ImportTree importTree = (ImportTree) tree;

        if (importTree.qualifiedIdentifier().toString().equals(FORBIDDEN_IMPORT)) {
            reportIssue(
                importTree,
                "Avoid importing " + FORBIDDEN_IMPORT + ". Prefer DTO or projection."
            );
        }
    }
}
