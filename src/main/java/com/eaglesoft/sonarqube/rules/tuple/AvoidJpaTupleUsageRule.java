package com.eaglesoft.sonarqube.rules.tuple;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.*;

import java.util.List;

@Rule(key = "AvoidJpaTupleUsage")
public class AvoidJpaTupleUsageRule extends IssuableSubscriptionVisitor {

    private static final String TUPLE_FQN = "javax.persistence.Tuple";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return List.of(
            Tree.Kind.VARIABLE,
            Tree.Kind.METHOD,
            Tree.Kind.PARAMETERIZED_TYPE,
            Tree.Kind.MEMBER_SELECT,
            Tree.Kind.IDENTIFIER
        );
    }

    @Override
    public void visitNode(Tree tree) {

        // Variable declaration
        if (tree.is(Tree.Kind.VARIABLE)) {
            VariableTree vt = (VariableTree) tree;
            checkType(vt.type(), vt);
        }

        // Method return type
        if (tree.is(Tree.Kind.METHOD)) {
            MethodTree mt = (MethodTree) tree;
            checkType(mt.returnType(), mt.simpleName());
        }

        // Generic types: List<Tuple>
        if (tree.is(Tree.Kind.PARAMETERIZED_TYPE)) {
            ParameterizedTypeTree pt = (ParameterizedTypeTree) tree;
            checkType(pt.type(), pt);
            pt.typeArguments().forEach(arg -> checkType(arg, pt));
        }

        // Fully qualified usage: javax.persistence.Tuple
        if (tree.is(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree ms = (MemberSelectExpressionTree) tree;
            Type type = ms.symbolType();
            if (isTuple(type)) {
                reportIssue(ms, "Avoid using javax.persistence.Tuple. Use DTO or projection.");
            }
        }

        // Simple identifier usage
        if (tree.is(Tree.Kind.IDENTIFIER)) {
            IdentifierTree id = (IdentifierTree) tree;
            Symbol symbol = id.symbol();
            if (symbol != null && isTuple(symbol.type())) {
                reportIssue(id, "Avoid using javax.persistence.Tuple. Use DTO or projection.");
            }
        }
    }

    private void checkType(Tree typeTree, Tree reportTree) {
        if (typeTree == null) return;

        Type type = ((ExpressionTree) typeTree).symbolType();
        if (isTuple(type)) {
            reportIssue(reportTree, "Avoid using javax.persistence.Tuple. Prefer DTO or projection.");
        }
    }

    private boolean isTuple(Type type) {
        return type != null && TUPLE_FQN.equals(type.fullyQualifiedName());
    }
}
