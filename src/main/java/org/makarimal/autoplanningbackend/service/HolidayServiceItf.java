package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.Holiday;

import java.util.List;

public interface HolidayServiceItf {
    Holiday createHoliday(Holiday holiday);
    List<Holiday> getAllHolidays();
    Holiday getHolidayById(Long id);
    void deleteHoliday(Long id);
}

