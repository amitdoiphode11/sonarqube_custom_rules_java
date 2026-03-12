package org.example;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;

public class YamlRulesDefinition implements RulesDefinition {
    public static final String REPO_KEY = "eaglesoft-yaml-repo";
    public static final String RULE_KEY = "RuleVersionSemanticCheck";

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPO_KEY, "yaml")
                .setName("Eaglesoft YAML Rules");

        repository.createRule(RULE_KEY)
                .setName("ruleVersion must follow Semantic Versioning")
                .setHtmlDescription("The <code>ruleVersion</code> field must follow the format <b>X.Y.Z</b> (e.g., 1.0.4).")
                .setSeverity(Severity.MAJOR)
                .setType(RuleType.CODE_SMELL)
                .addTags("convention", "yaml");

        repository.done();
    }
}
