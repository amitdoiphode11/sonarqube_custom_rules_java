package org.example;

import org.sonar.api.Plugin;

public class YamlCustomPlugin implements Plugin {
    @Override
    public void define(Context context) {
        // Register the rule metadata for the Web UI
        context.addExtension(YamlRulesDefinition.class);

        // Register the scanning engine
        context.addExtension(YamlVersionSensor.class);
    }
}