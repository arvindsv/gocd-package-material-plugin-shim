package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.thoughtworks.go.plugin.api.response.validation.ValidationError;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationUtil {
    public static List<Map<String, String>> toNewStyleValidationResult(ValidationResult oldValidationResult) {
        List<Map<String, String>> newValidationResult = new ArrayList<Map<String, String>>();

        for (ValidationError oldValidationError : oldValidationResult.getErrors()) {
            Map<String, String> errorMapForOneField = new HashMap<String, String>();
            errorMapForOneField.put("key", oldValidationError.getKey());
            errorMapForOneField.put("message", oldValidationError.getMessage());
            newValidationResult.add(errorMapForOneField);
        }

        return newValidationResult;
    }
}
