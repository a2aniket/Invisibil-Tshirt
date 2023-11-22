/**
 * Repository interface for performing database operations using JPA.
 */
package com.persistent.service;

import com.persistent.model.StudentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StudentRepository extends JpaRepository<StudentDto, Long> {
    
}