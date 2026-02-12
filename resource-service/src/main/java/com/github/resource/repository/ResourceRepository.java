package com.github.resource.repository;

import com.github.resource.model.Resource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ResourceRepository extends ReactiveCrudRepository<Resource, Long> {
}
