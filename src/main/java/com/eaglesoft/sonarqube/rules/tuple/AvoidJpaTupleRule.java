package com.eaglesoft.sonarqube.rules.tuple;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;

@Rule(key = "avoid-tuples")
public class AvoidJpaTupleRule extends IssuableSubscriptionVisitor {

  private static final String TUPLE = "javax.persistence.Tuple";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return List.of(Tree.Kind.IMPORT, Tree.Kind.VARIABLE, Tree.Kind.METHOD);
  }

  @Override
  public void visitNode(Tree tree) {
    if (tree.is(Tree.Kind.IMPORT)) {
      ImportTree it = (ImportTree) tree;
      if (it.qualifiedIdentifier().toString().equals(TUPLE)) {
        reportIssue(it, "Avoid using javax.persistence.Tuple. Prefer DTO or projection.");
      }
    }

    if (tree.is(Tree.Kind.VARIABLE)) {
      VariableTree vt = (VariableTree) tree;
      Type type = vt.type().symbolType();
      if (type.is(TUPLE)) {
        reportIssue(vt, "Avoid Tuple usage. Prefer typed DTO.");
      }
    }

    if (tree.is(Tree.Kind.METHOD)) {
      MethodTree mt = (MethodTree) tree;
      if (mt.symbol().returnType().equals(TUPLE)) {
        reportIssue(mt.simpleName(), "Method returns Tuple. Use DTO or projection.");
      }
    }
  }
}
