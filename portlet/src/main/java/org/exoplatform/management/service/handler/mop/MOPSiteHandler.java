package org.exoplatform.management.service.handler.mop;

import org.exoplatform.management.service.api.AbstractResourceHandler;
import org.exoplatform.management.service.api.StagingService;
import org.exoplatform.portal.mop.SiteType;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class MOPSiteHandler extends AbstractResourceHandler {

  String parentPath;

  public MOPSiteHandler(SiteType siteType) {
    parentPath = "/site/" + siteType.getName() + "sites/";
  }

  @Override
  public String getParentPath() {
    return parentPath;
  }

  @Override
  public boolean synchronizeData(Set<String> resources, boolean isSSL, String host, String port, String username, String password, Map<String, String> options) {
    Set<String> selectedResources = filterSubResources(resources);
    if (selectedResources == null || selectedResources.isEmpty()) {
      return false;
    }

    Map<String, String> selectedExportOptions = filterOptions(options, OPERATION_EXPORT_PREFIX, false);

    for (String resourcePath : selectedResources) {
      File file = getExportedFileFromOperation(resourcePath, selectedExportOptions);
      synhronizeData(file, isSSL, host, port, StagingService.SITES_PARENT_PATH, username, password, filterOptions(options, OPERATION_IMPORT_PREFIX, false));
    }
    clearTempFiles();
    return true;
  }
}