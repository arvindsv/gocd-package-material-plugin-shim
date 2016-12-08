package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.thoughtworks.go.plugin.api.config.Property;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProvider;
import com.thoughtworks.go.plugin.api.material.packagerepository.RepositoryConfiguration;
import com.thoughtworks.go.plugin.api.response.Result;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.config.Property.*;

public class RepositoryConfigurationRequestHandler {
    private PackageMaterialProvider oldProvider;

    public RepositoryConfigurationRequestHandler(PackageMaterialProvider oldProvider) {
        this.oldProvider = oldProvider;
    }

    public Map handleConfiguration() {
        RepositoryConfiguration oldConfig = oldProvider.getConfig().getRepositoryConfiguration();
        Map<String, Map> newConfig = new HashMap<String, Map>();

        for (Property oldProperty : oldConfig.list()) {
            newConfig.put(oldProperty.getKey(), ConfigUtil.createConfigurationField(
                    oldProperty.getOption(DISPLAY_NAME),
                    String.valueOf(oldProperty.getOption(DISPLAY_ORDER)),
                    oldProperty.getOption(SECURE),
                    oldProperty.getOption(PART_OF_IDENTITY),
                    oldProperty.getOption(REQUIRED)));
        }

        return newConfig;
    }

    public List handleValidateConfiguration(Map newStyleRequestMapContainingRepoConfig) {
        ValidationResult oldValidationResult = oldProvider.getConfig().isRepositoryConfigurationValid(ConfigUtil.toOldStyleRepoConfig(newStyleRequestMapContainingRepoConfig));
        return ValidationUtil.toNewStyleValidationResult(oldValidationResult);
    }

    public Map handleCheckRepositoryConnection(Map newStyleRequestMapContainingRepoConfig) {
        Result oldResult = oldProvider.getPoller().checkConnectionToRepository(ConfigUtil.toOldStyleRepoConfig(newStyleRequestMapContainingRepoConfig));
        Map<String, Object> newResult = new HashMap<String, Object>();

        newResult.put("status", oldResult.isSuccessful() ? "success" : "failure");
        newResult.put("messages", oldResult.getMessages());

        return newResult;
    }

}
