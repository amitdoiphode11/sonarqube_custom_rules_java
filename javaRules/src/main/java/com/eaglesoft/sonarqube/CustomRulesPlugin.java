package com.eaglesoft.sonarqube;

import org.sonar.api.Plugin;

public class CustomRulesPlugin implements Plugin {
    @Override
    public void define(Context context) {
        context.addExtension(JavaRulesDefinition.class);
        context.addExtension(JavaCheckRegistrar.class);
    }
}
