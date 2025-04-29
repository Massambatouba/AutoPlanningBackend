package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.WeeklySiteRequirement;

import java.util.List;

public interface WeeklySiteRequirementServiceItf {

    public List<WeeklySiteRequirement> getAll();

    public WeeklySiteRequirement save(WeeklySiteRequirement req);

    public void delete(Long id);

    List<WeeklySiteRequirement> getBySite(Long siteId);


}
