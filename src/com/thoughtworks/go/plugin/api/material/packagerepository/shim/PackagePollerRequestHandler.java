package com.thoughtworks.go.plugin.api.material.packagerepository.shim;

import com.thoughtworks.go.plugin.api.material.packagerepository.PackageMaterialProvider;
import com.thoughtworks.go.plugin.api.material.packagerepository.PackageRevision;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.material.packagerepository.shim.ConfigUtil.toOldStylePackageConfig;
import static com.thoughtworks.go.plugin.api.material.packagerepository.shim.ConfigUtil.toOldStyleRepoConfig;

public class PackagePollerRequestHandler {
    private PackageMaterialProvider oldProvider;

    public PackagePollerRequestHandler(PackageMaterialProvider oldProvider) {
        this.oldProvider = oldProvider;
    }

    public Map handleLatestRevision(Map request) {
        PackageRevision oldStyleLatestRevision = oldProvider.getPoller().getLatestRevision(toOldStylePackageConfig(request), toOldStyleRepoConfig(request));

        return toNewStyleMapForRevision(oldStyleLatestRevision);
    }

    public Map handleLatestRevisionSince(Map request) {
        PackageRevision oldStyleLatestRevisionSince = oldProvider.getPoller().latestModificationSince(
                toOldStylePackageConfig(request), toOldStyleRepoConfig(request), toOldStylePreviousRevision(request));
        if (oldStyleLatestRevisionSince == null) {
            return new HashMap();
        }

        return toNewStyleMapForRevision(oldStyleLatestRevisionSince);
    }

    private Map toNewStyleMapForRevision(PackageRevision oldStyleRevision) {
        Map<String, Object> newStyleLatestRevision = new HashMap<String, Object>();
        newStyleLatestRevision.put("revision", oldStyleRevision.getRevision());
        newStyleLatestRevision.put("timestamp", formatTimestamp(oldStyleRevision.getTimestamp()));
        newStyleLatestRevision.put("user", oldStyleRevision.getUser());
        newStyleLatestRevision.put("revisionComment", oldStyleRevision.getRevisionComment());
        newStyleLatestRevision.put("trackbackUrl", oldStyleRevision.getTrackbackUrl());
        newStyleLatestRevision.put("data", oldStyleRevision.getData());
        return newStyleLatestRevision;
    }

    private PackageRevision toOldStylePreviousRevision(Map requestContainingPreviousRevision) {
        Map<String, Object> newStylePreviousRevisionValues = (Map<String, Object>) requestContainingPreviousRevision.get("previous-revision");
        String revision = (String) newStylePreviousRevisionValues.get("revision");
        Date timestamp = fromTimestamp((String) newStylePreviousRevisionValues.get("timestamp"));
        Map<String, String> data = (Map<String, String>) newStylePreviousRevisionValues.get("data");

        return new PackageRevision(revision, timestamp, null, data);
    }

    private Date fromTimestamp(String timestamp) {
        try {
            return dateFormat().parse(timestamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatTimestamp(Date timestamp) {
        return dateFormat().format(timestamp);
    }

    private SimpleDateFormat dateFormat() {
        /* Never cache a SimpleDateFormat. It's not thread-safe. */
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }
}
