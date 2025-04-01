package com.fengzhi.event_manager.controller;

import com.fengzhi.event_manager.pojo.Event;
import com.fengzhi.event_manager.pojo.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@Validated
public class EventController {
    @GetMapping("/list")
    public Result list(@RequestHeader("Authorization") String token) {
        return Result.success("hello");
    }
}
