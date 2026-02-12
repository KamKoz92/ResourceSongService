package com.github.resource.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyResourceJPARepository extends JpaRepository<MyResourceJPA, Long> {
}