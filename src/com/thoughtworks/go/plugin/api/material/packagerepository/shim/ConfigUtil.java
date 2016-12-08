package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.thoughtworks.go.plugin.api.config.Property;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageConfiguration;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProperty;
import com.thoughtworks.go.plugin.api.material.packagerepository.RepositoryConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
    public static Map createConfigurationField(String displayName, String displayOrder, boolean secure, boolean partOfIdentity, boolean required) {
        Map configMap = new HashMap();
        configMap.put("display-name", displayName);
        configMap.put("display-order", displayOrder);
        configMap.put("secure", secure);
        configMap.put("part-of-identity", partOfIdentity);
        configMap.put("required", required);

        return configMap;
    }

    public static RepositoryConfiguration toOldStyleRepoConfig(Map newStyleRequestMapContainingRepoConfig) {
        RepositoryConfiguration oldStyleRepoConfig = new RepositoryConfiguration();
        Map<String, Map<String, String>> newStyleRepoConfigValues = (Map<String, Map<String, String>>) newStyleRequestMapContainingRepoConfig.get("repository-configuration");

        for (String key : newStyleRepoConfigValues.keySet()) {
            oldStyleRepoConfig.add(new PackageMaterialProperty(key, newStyleRepoConfigValues.get(key).get("value")));
        }

        return oldStyleRepoConfig;
    }

    public static PackageConfiguration toOldStylePackageConfig(Map newStyleRequestMapContainingPackageConfig) {
        PackageConfiguration oldStylePackageConfig = new PackageConfiguration();
        Map<String, Map<String, String>> newStylePackageConfigValues = (Map<String, Map<String, String>>) newStyleRequestMapContainingPackageConfig.get("package-configuration");

        for (String key : newStylePackageConfigValues.keySet()) {
            oldStylePackageConfig.add(new PackageMaterialProperty(key, newStylePackageConfigValues.get(key).get("value")));
        }

        return oldStylePackageConfig;
    }
}
