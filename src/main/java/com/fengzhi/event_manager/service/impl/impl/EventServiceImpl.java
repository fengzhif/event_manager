package com.fengzhi.event_manager.service.impl.impl;

import com.fengzhi.event_manager.mapper.EventMapper;
import com.fengzhi.event_manager.pojo.Event;
import com.fengzhi.event_manager.pojo.PageBean;
import com.fengzhi.event_manager.service.impl.EventService;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {
    EventMapper eventMapper;
    @Autowired
    public EventServiceImpl(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }
    @Override
    public void add(Event event) {
        Map<String, Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        event.setCreateUser(id);
        event.setCreateTime(LocalDateTime.now());
        event.setUpdateTime(LocalDateTime.now());
        eventMapper.add(event);
    }

    @Override
    public PageBean<Event> getEventList(Integer pageNum, Integer pageSize, Integer categoryId, String state, String startDate, String endDate) {
        //新建一个pagebean
        PageBean<Event> pageBean = new PageBean<>();
        //启动pagehelper
        PageHelper.startPage(pageNum, pageSize);
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer createUser = (Integer) map.get("id");
        String role = (String) map.get("role");
        //查询事件类别
        List<Event> eventList;
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;
        if(role.equals("ADMIN")){
            eventList=eventMapper.getAllEventList(categoryId,state, start, end);
        }
        else{
            eventList=eventMapper.getEventList(createUser,categoryId,state, start, end);
        }
        //强转成page类型来获取pagebean所需数据
        Page<Event> eventPage=(Page<Event>)eventList;
        pageBean.setTotal((int) eventPage.getTotal());
        pageBean.setItems(eventPage.getResult());
        return pageBean;
    }

    @Override
    public Event findEventById(Integer id) {
        return eventMapper.findEventById(id);
    }


    @Override
    public void updateEvent(Event event) {
        event.setUpdateTime(LocalDateTime.now());
        eventMapper.updateEvent(event);
    }

    @Override
    public void deleteEvent(Integer id) {
        eventMapper.deleteEvent(id);
    }

}
