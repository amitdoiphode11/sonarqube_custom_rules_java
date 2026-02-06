package com.eaglesoft.sonarqube.rules.tuple;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

@Rule(key = "AvoidConfigurableImport")
public class AvoidConfigurableImportRule extends IssuableSubscriptionVisitor {

    @RuleProperty(
        key = "forbiddenImport",
        description = "Fully qualified class name to forbid",
        defaultValue = "javax.persistence.Tuple"
    )
    public String forbiddenImport;

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(Tree.Kind.IMPORT);
    }

    @Override
    public void visitNode(Tree tree) {
        ImportTree importTree = (ImportTree) tree;

        if (importTree.qualifiedIdentifier().toString().equals(forbiddenImport)) {
            reportIssue(
                importTree,
                "Avoid importing " + forbiddenImport
            );
        }
    }
}
