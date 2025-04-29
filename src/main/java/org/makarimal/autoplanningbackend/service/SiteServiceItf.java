package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.Site;

import java.util.List;
import java.util.Optional;

public interface SiteServiceItf {
    public List<Site> getAll();

    public Site getById(Long id);

    Optional<Site> getSiteById(Long siteId);


    Site createSite(Site site);

    public Site updateSite(Long id, Site updated);

    public void deleteSite(Long id);
}
