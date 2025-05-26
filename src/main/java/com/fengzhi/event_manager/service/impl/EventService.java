package com.fengzhi.event_manager.service.impl;

import com.fengzhi.event_manager.pojo.Event;
import com.fengzhi.event_manager.pojo.PageBean;

public interface EventService {
    void add(Event event);

    PageBean<Event> getEventList(Integer pageNum, Integer pageSize, Integer categoryId, String state, String startDate, String endDate);

    void updateEvent(Event event);

    Event findEventById(Integer id);

    void deleteEvent(Integer id);
}
