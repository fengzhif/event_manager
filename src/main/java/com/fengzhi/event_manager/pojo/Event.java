package com.fengzhi.event_manager.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fengzhi.event_manager.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Event {
    @NotNull(groups = Update.class)
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;
    @NotEmpty
    private String content;
    @URL
    private String coverImg;
    @State
    private String state;
    @NotNull
    private Integer categoryId;
    @JsonIgnore
    private Integer createUser;
    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime updateTime;

    public interface Update  extends Default {}
    public interface Add extends Default {}

}
