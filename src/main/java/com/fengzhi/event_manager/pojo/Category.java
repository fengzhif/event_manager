package com.fengzhi.event_manager.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    @NotNull(groups = Update.class)
    private Integer id;
    @NotEmpty
    private String categoryName;
    @NotEmpty
    private String categoryAlias;
    @JsonIgnore
    private Integer createUser;
    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime updateTime;

    public interface Add extends Default {
    }

    public interface Update extends Default {
    }
}
