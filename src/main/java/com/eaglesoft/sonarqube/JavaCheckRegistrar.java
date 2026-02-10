package com.eaglesoft.sonarqube;

import com.eaglesoft.sonarqube.rules.concat.NoQueryConcatRule;
import com.eaglesoft.sonarqube.rules.*;
import com.eaglesoft.sonarqube.rules.jpa.JpaBestPracticesCheckRule;
import com.eaglesoft.sonarqube.rules.lazy.NoEagerFetchCollectionRule;
import com.eaglesoft.sonarqube.rules.stream.PreferStreamApiRule1;
import com.eaglesoft.sonarqube.rules.tuple.AvoidConfigurableImportRule;
import com.eaglesoft.sonarqube.rules.tuple.AvoidJpaTupleRule;
import com.eaglesoft.sonarqube.rules.tuple.AvoidJpaTupleUsageRule;
import com.eaglesoft.sonarqube.rules.tuple.AvoidSpecificImportRule;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.Arrays;
import java.util.List;

public class JavaCheckRegistrar implements CheckRegistrar {
    @Override
    public void register(RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(
                JavaRulesDefinition.REPOSITORY_KEY,
                checkClasses(),
                testCheckClasses()
        );
    }

    private static Iterable<Class<? extends JavaCheck>> checkClasses() {
        return Arrays.asList(
                DetectClassExtendThread.class,
                EmptyCatchBlockRule.class,
                HardCodeCredentialRule.class,
                LongMethodRule.class,
                NoSystemOutRule.class,
                AvoidJpaTupleRule.class,
                AvoidSpecificImportRule.class,
                AvoidConfigurableImportRule.class,
                AvoidJpaTupleUsageRule.class,
                PreferStreamApiRule1.class,
                JpaBestPracticesCheckRule.class,
                NoQueryConcatRule.class,
                NoEagerFetchCollectionRule.class
        );
    }

    private static Iterable<Class<? extends JavaCheck>> testCheckClasses() {
        return List.of();
    }
}
