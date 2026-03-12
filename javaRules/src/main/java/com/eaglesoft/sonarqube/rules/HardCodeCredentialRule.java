package com.eaglesoft.sonarqube.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Rule(key = "hardcoded-credential")
public class HardCodeCredentialRule extends IssuableSubscriptionVisitor {

    private static final List<String> CREDENTIAL_KEYWORDS = Arrays.asList(
            "password", "passwd", "pwd",
            "secret", "token", "tkn",
            "apikey", "api_key",
            "accesskey", "access_key"
    );

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.VARIABLE);
    }

    @Override
    public void visitNode(Tree tree) {
        VariableTree variableTree = (VariableTree) tree;

        if (variableTree.type() == null ||
                !"String".equals(variableTree.type().symbolType().name())
        ) {
            return;
        }

        ExpressionTree initializer = variableTree.initializer();
        if (initializer == null) {
            return;
        }

        if (!initializer.is(Tree.Kind.STRING_LITERAL)){
            return;
        }

        String variableName = variableTree.simpleName().name().toLowerCase();

        boolean isCredential = CREDENTIAL_KEYWORDS.stream()
                .anyMatch(variableName::contains);

        if (isCredential) {
            reportIssue(variableTree.simpleName(),
                    "Hard-coded credential detected. Avoid embedding secrets directly in source code.");
        }
    }

}
