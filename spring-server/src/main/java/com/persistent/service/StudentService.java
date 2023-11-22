/**
 * Service class for handling CRUD operations on Student resource.
 */
package com.persistent.service;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import com.persistent.model.StudentDto;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import com.persistent.exception.InvalidIdException;
import com.persistent.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.persistent.util.Constants;


@Service
@Log4j2
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method is used to retrieve the student list from the database.
     * @return On successful operation the student list.
     *         or Warning message as "No student found!".
     * @param pageNumber Page number to be displayed.
     * @param pageSize Number of records to be displayed per page.
     * @param sortBy Column name to sort the list by.
     * @param sortDir Sort direction i.e. ASC or DESC.
     */
    public List<StudentDto> getStudentList(String queryString, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        
        log.info("Getting student list");
        String message = null;
        Sort sort = null;
        
        // Checking and correcting the input values
        pageNumber = PaginationAndSorting.checkPageNumber(pageNumber);
        pageSize = PaginationAndSorting.checkPageSize(pageSize);
        sortBy = PaginationAndSorting.checkColumnName(StudentDto.class,sortBy);
        sortDir = PaginationAndSorting.checkSortDirection(sortDir);
        sort = PaginationAndSorting.setSortObject(sortBy, sortDir);
       
        // Creating the page request object.
        // Page number starts from 0.
        // So, we are decrementing the page number by 1.
        // For example, if page number is 1 then it will be 0.
        Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);

        List<StudentDto> studentList = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        
        studentList = Searching.generatePredicateList(StudentDto.class,queryString, entityManager,criteriaBuilder, sortBy, sortDir, pageable);
       
        if (studentList == null) {
            message = "No student found!";
            log.warn(message);
        }
        return studentList;
    }

    /**
     * This method is used to retrieve a student with the specified ID.
     * @param id The ID of the student to retrieve.
     * @return On successful operation the student with specified ID.
     * @throws InvalidIdException If incorrect ID is provided.
     * @throws ResourceNotFoundException If the student object is not found. 
     */
    public StudentDto getStudent(Long id) {
        log.info("Getting student with id {}", id);

        if(id < 1) {
            String message = "Invalid id : "+ id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<StudentDto> student = studentRepository.findById(id);
        
        if(student.isPresent()) {
            return student.get();
        } else {
            String message = "Student with id " + id + " is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * This method adds a new student to the database with given information.
     * @param student The student object that needs to be added.
     * @return On successful operation the added student is returned.
     */
    public StudentDto addStudent(StudentDto student ) {
        log.info("Adding student");
        return studentRepository.save(student);
    }

    /**
     * This method deletes the student to the database with given information.
     * @param id The id of student that needs to be deleted.
     * @return On successful operation the deleted student is returned.
     * @throws InvalidIdException If incorrect ID is provided.
     * @throws ResourceNotFoundException If the student object is not found.
     */
    public StudentDto deleteStudent(Long id) {
        log.info("Started processing of delete operation");

        if(id < 1) {
            String message = "Invalid id : "+id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<StudentDto> student = studentRepository.findById(id);
        
        if(student.isPresent()) {
            studentRepository.deleteById(id);
            log.info("Successfully completed the deletion operation for ID : {}",id);
            return student.get();
        } else {
            String message = "Student with id "+id+" is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * This method updates an existing student in the database.
     * @param student The student object that needs to be updated.
     * @return On successful operation the updated student object is returned.
     * @throws ResourceNotFoundException If the student object is not found.
     */
    public StudentDto updateStudent(StudentDto student ) {
        log.info("Updating student with id {}",student.getId());
        long id = student.getId();

        if(id < 1) {
            String message = "Invalid id : "+id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<StudentDto> studentToBeUpdated = studentRepository.findById(id);
        
        if(studentToBeUpdated.isPresent()) {
            return studentRepository.save(student);
        } else {
            String message = "Student with id "+id+" is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

}
