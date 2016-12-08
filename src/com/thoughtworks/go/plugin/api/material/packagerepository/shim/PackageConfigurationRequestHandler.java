package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.thoughtworks.go.plugin.api.config.Property;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageConfiguration;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProvider;
import com.thoughtworks.go.plugin.api.response.Result;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.material.packagerepository.shim.ConfigUtil.toOldStylePackageConfig;
import static com.thoughtworks.go.plugin.api.material.packagerepository.shim.ConfigUtil.toOldStyleRepoConfig;

import static com.thoughtworks.go.plugin.api.config.Property.*;

public class PackageConfigurationRequestHandler {
    private PackageMaterialProvider oldProvider;

    public PackageConfigurationRequestHandler(PackageMaterialProvider oldProvider) {
        this.oldProvider = oldProvider;
    }

    public Map handleConfiguration() {
        PackageConfiguration oldConfig = oldProvider.getConfig().getPackageConfiguration();
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

    public List handleValidateConfiguration(Map request) {
        ValidationResult oldValidationResult = oldProvider.getConfig().isPackageConfigurationValid(toOldStylePackageConfig(request), toOldStyleRepoConfig(request));
        return ValidationUtil.toNewStyleValidationResult(oldValidationResult);
    }

    public Map handleCheckPackageConnection(Map request) {
        Result oldResult = oldProvider.getPoller().checkConnectionToPackage(toOldStylePackageConfig(request), toOldStyleRepoConfig(request));
        Map<String, Object> newResult = new HashMap<String, Object>();

        newResult.put("status", oldResult.isSuccessful() ? "success" : "failure");
        newResult.put("messages", oldResult.getMessages());

        return newResult;
    }
}
