package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Event;
import com.fengzhi.event_manager.pojo.PageBean;
import com.fengzhi.event_manager.pojo.Result;
import com.fengzhi.event_manager.service.impl.EventService;
import com.fengzhi.event_manager.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/event")
@Validated
public class EventController {
    EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @PostMapping
    public Result addEvent(@Validated(Event.Add.class) @RequestBody Event event) {
            eventService.add(event);
            return Result.success();
    }

    @GetMapping
    public Result<PageBean<Event>> getEventList(Integer pageNum,
                                                Integer pageSize,
                                                @RequestParam(required = false) Integer categoryId,
                                                @RequestParam(required = false) String state,
                                                @RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate) {
        PageBean<Event> pb=eventService.getEventList(pageNum,pageSize,categoryId,state,startDate,endDate);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result<Event> getEventDetail(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        String userRole=(String) map.get("role");
        Event event=eventService.findEventById(id);
        //管理员可以查看所有事件
        if(userRole.equals("ADMIN") || userRole.equals("SUPER")){
            return Result.success(event);
        }
        else if(!userId.equals(event.getCreateUser())){
            return Result.error("仅可查看自己创建的事件");
        }
        return Result.success(event);
    }

    @PutMapping
    public Result updateEvent(@Validated(Event.Update.class) @RequestBody Event event) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        String userRole=(String) map.get("role");
        Integer id=event.getId();
        Integer createUserId= eventService.findEventById(id).getCreateUser();
        if(!userId.equals(createUserId) && !userRole.equals("SUPER")){
            return Result.error("只有事件创建者可以修改事件信息");
        }
        eventService.updateEvent(event);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteEvent(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        String userRole=(String) map.get("role");
        Integer createUserId= eventService.findEventById(id).getCreateUser();
        if(!userId.equals(createUserId) && !userRole.equals("SUPER")){
            return Result.error("只有事件创建者可以删除事件信息");
        }
        eventService.deleteEvent(id);
        return Result.success();
    }
}
