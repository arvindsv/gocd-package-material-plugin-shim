package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProvider;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Arrays;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.material.packagerepository.shim.Constants.*;

public class ReplacementProvider {

    private final PackageMaterialProvider oldProvider;

    public ReplacementProvider(PackageMaterialProvider oldProvider) {
        this.oldProvider = oldProvider;
    }

    public GoPluginApiResponse handle(GoPluginApiRequest goPluginApiRequest) throws UnhandledRequestTypeException {

        Object result = null;
        String requestName = goPluginApiRequest.requestName();
        Map requestBodyMap = (Map) new GsonBuilder().create().fromJson(goPluginApiRequest.requestBody(), Object.class);

        RepositoryConfigurationRequestHandler repositoryConfigHandler = new RepositoryConfigurationRequestHandler(oldProvider);
        PackageConfigurationRequestHandler packageConfigHandler = new PackageConfigurationRequestHandler(oldProvider);
        PackagePollerRequestHandler packagePollerHandler = new PackagePollerRequestHandler(oldProvider);

        if (requestName.equals(REPOSITORY_CONFIGURATION)) {
            result = repositoryConfigHandler.handleConfiguration();
        } else if (requestName.equals(VALIDATE_REPOSITORY_CONFIGURATION)) {
            result = repositoryConfigHandler.handleValidateConfiguration(requestBodyMap);
        } else if (requestName.equals(CHECK_REPOSITORY_CONNECTION)) {
            result = repositoryConfigHandler.handleCheckRepositoryConnection(requestBodyMap);
        } else if (requestName.equals(PACKAGE_CONFIGURATION)) {
            result = packageConfigHandler.handleConfiguration();
        } else if (requestName.equals(VALIDATE_PACKAGE_CONFIGURATION)) {
            result = packageConfigHandler.handleValidateConfiguration(requestBodyMap);
        } else if (requestName.equals(CHECK_PACKAGE_CONNECTION)) {
            result = packageConfigHandler.handleCheckPackageConnection(requestBodyMap);
        } else if (requestName.equals(LATEST_REVISION)) {
            result = packagePollerHandler.handleLatestRevision(requestBodyMap);
        } else if (requestName.equals(LATEST_REVISION_SINCE)) {
            result = packagePollerHandler.handleLatestRevisionSince(requestBodyMap);
        }

        if (result != null) {
            return createResponse(SUCCESS_RESPONSE_CODE, result);
        }

        return null;
    }

    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier("package-repository", Arrays.asList("1.0"));
    }

    private static GoPluginApiResponse createResponse(int responseCode, Object body) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        response.setResponseBody(new GsonBuilder().serializeNulls().create().toJson(body));
        return response;
    }
}
