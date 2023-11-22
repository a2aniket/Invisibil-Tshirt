/**
 * Repository interface for performing database operations using JPA.
 */
package com.persistent.service;

import com.persistent.model.CourseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CourseRepository extends JpaRepository<CourseDto, Long> {
    
}