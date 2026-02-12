package com.github.resource.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("resources")
public class Resource {
    @Id
    private Long id;
    private byte[] audio;
}