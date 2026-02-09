package com.github.resource.repository;

import com.github.resource.model.ResourceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ResourceRepository extends ReactiveCrudRepository<ResourceEntity, Long> {
}
