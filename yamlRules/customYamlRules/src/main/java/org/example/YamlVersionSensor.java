package org.example;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.io.IOException;
import java.util.regex.Pattern;

public class YamlVersionSensor implements Sensor {
    private static final Logger LOG = Loggers.get(YamlVersionSensor.class);
    private static final Pattern SEMVER_PATTERN = Pattern.compile("ruleVersion:\\s*[\"']?\\d+\\.\\d+\\.\\d+[\"']?");

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("Eaglesoft YAML SemVer Check").onlyOnLanguage("yaml");
    }

    @Override
    public void execute(SensorContext context) {
        FileSystem fs = context.fileSystem();
        Iterable<InputFile> yamlFiles = fs.inputFiles(fs.predicates().hasLanguage("yaml"));

        for (InputFile file : yamlFiles) {
            try {
                String[] lines = file.contents().split("\\r?\\n");
                for (int i = 0; i < lines.length; i++) {
                    String currentLine = lines[i];
                    if (currentLine.contains("ruleVersion:") && !SEMVER_PATTERN.matcher(currentLine).find()) {
                        reportIssue(context, file, i + 1);
                    }
                }
            } catch (IOException e) {
                LOG.error("Failed to read: " + file.filename(), e);
            }
        }
    }

    private void reportIssue(SensorContext context, InputFile file, int lineNum) {
        NewIssue issue = context.newIssue().forRule(RuleKey.of(YamlRulesDefinition.REPO_KEY, YamlRulesDefinition.RULE_KEY));
        issue.at(issue.newLocation().on(file).at(file.selectLine(lineNum))
                        .message("This version does not follow SemVer (e.g., 1.0.4)"))
                .save();
    }
}