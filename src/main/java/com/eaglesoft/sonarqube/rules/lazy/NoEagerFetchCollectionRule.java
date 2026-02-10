package com.eaglesoft.sonarqube.rules.lazy;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.plugins.java.api.semantic.Type;

import java.util.Arrays;
import java.util.List;

@Rule(key = "no-eager-fetch-collections")
public class NoEagerFetchCollectionRule extends IssuableSubscriptionVisitor {

    private static final List<String> COLLECTION_ANNOTATIONS = Arrays.asList(
            "javax.persistence.OneToMany",
            "javax.persistence.ManyToMany",
            "jakarta.persistence.OneToMany",
            "jakarta.persistence.ManyToMany"
    );

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.VARIABLE);
    }

    @Override
    public void visitNode(Tree tree) {
        VariableTree variableTree = (VariableTree) tree;

        variableTree.modifiers().forEach(modifier -> {
            if (modifier.is(Tree.Kind.ANNOTATION)) {
                AnnotationTree annotation = (AnnotationTree) modifier;
                if (isCollectionRelationship(annotation)) {
                    checkEagerFetch(annotation);
                }
            }
        });
    }

    private boolean isCollectionRelationship(AnnotationTree annotation) {
        Type annotationType = annotation.annotationType().symbolType();
        // Manually check if the type matches any in our list
        return COLLECTION_ANNOTATIONS.stream().anyMatch(annotationType::is);
    }

    private void checkEagerFetch(AnnotationTree annotation) {
        for (ExpressionTree argument : annotation.arguments()) {
            if (argument.is(Tree.Kind.ASSIGNMENT)) {
                AssignmentExpressionTree assignment = (AssignmentExpressionTree) argument;

                // Inspect the identifier on the left side of the '='
                if (assignment.variable().is(Tree.Kind.IDENTIFIER) &&
                        "fetch".equals(assignment.variable().toString())) {

                    if (isEagerConstant(assignment.expression())) {
                        reportIssue(assignment.expression(), "Collections should use FetchType.LAZY to avoid N+1 performance issues.");
                    }
                }
            }
        }
    }

    private boolean isEagerConstant(ExpressionTree expression) {
        // This handles FetchType.EAGER
        if (expression.is(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree mset = (MemberSelectExpressionTree) expression;
            return "EAGER".equals(mset.identifier().name());
        }
        // This handles EAGER if statically imported
        if (expression.is(Tree.Kind.IDENTIFIER)) {
            return "EAGER".equals(expression.toString());
        }
        return false;
    }
}
