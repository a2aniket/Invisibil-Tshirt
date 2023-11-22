/**
 * Service class for handling CRUD operations on Course resource.
 */
package com.persistent.service;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import com.persistent.model.CourseDto;

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
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method is used to retrieve the course list from the database.
     * @return On successful operation the course list.
     *         or Warning message as "No course found!".
     * @param pageNumber Page number to be displayed.
     * @param pageSize Number of records to be displayed per page.
     * @param sortBy Column name to sort the list by.
     * @param sortDir Sort direction i.e. ASC or DESC.
     */
    public List<CourseDto> getCourseList(String queryString, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        
        log.info("Getting course list");
        String message = null;
        Sort sort = null;
        
        // Checking and correcting the input values
        pageNumber = PaginationAndSorting.checkPageNumber(pageNumber);
        pageSize = PaginationAndSorting.checkPageSize(pageSize);
        sortBy = PaginationAndSorting.checkColumnName(CourseDto.class,sortBy);
        sortDir = PaginationAndSorting.checkSortDirection(sortDir);
        sort = PaginationAndSorting.setSortObject(sortBy, sortDir);
       
        // Creating the page request object.
        // Page number starts from 0.
        // So, we are decrementing the page number by 1.
        // For example, if page number is 1 then it will be 0.
        Pageable pageable = PageRequest.of(--pageNumber, pageSize, sort);

        List<CourseDto> courseList = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        
        courseList = Searching.generatePredicateList(CourseDto.class,queryString, entityManager,criteriaBuilder, sortBy, sortDir, pageable);
       
        if (courseList == null) {
            message = "No course found!";
            log.warn(message);
        }
        return courseList;
    }

    /**
     * This method is used to retrieve a course with the specified ID.
     * @param id The ID of the course to retrieve.
     * @return On successful operation the course with specified ID.
     * @throws InvalidIdException If incorrect ID is provided.
     * @throws ResourceNotFoundException If the course object is not found. 
     */
    public CourseDto getCourse(Long id) {
        log.info("Getting course with id {}", id);

        if(id < 1) {
            String message = "Invalid id : "+ id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<CourseDto> course = courseRepository.findById(id);
        
        if(course.isPresent()) {
            return course.get();
        } else {
            String message = "Course with id " + id + " is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * This method adds a new course to the database with given information.
     * @param course The course object that needs to be added.
     * @return On successful operation the added course is returned.
     */
    public CourseDto addCourse(CourseDto course ) {
        log.info("Adding course");
        return courseRepository.save(course);
    }

    /**
     * This method deletes the course to the database with given information.
     * @param id The id of course that needs to be deleted.
     * @return On successful operation the deleted course is returned.
     * @throws InvalidIdException If incorrect ID is provided.
     * @throws ResourceNotFoundException If the course object is not found.
     */
    public CourseDto deleteCourse(Long id) {
        log.info("Started processing of delete operation");

        if(id < 1) {
            String message = "Invalid id : "+id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<CourseDto> course = courseRepository.findById(id);
        
        if(course.isPresent()) {
            courseRepository.deleteById(id);
            log.info("Successfully completed the deletion operation for ID : {}",id);
            return course.get();
        } else {
            String message = "Course with id "+id+" is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

    /**
     * This method updates an existing course in the database.
     * @param course The course object that needs to be updated.
     * @return On successful operation the updated course object is returned.
     * @throws ResourceNotFoundException If the course object is not found.
     */
    public CourseDto updateCourse(CourseDto course ) {
        log.info("Updating course with id {}",course.getId());
        long id = course.getId();

        if(id < 1) {
            String message = "Invalid id : "+id;
            log.error(message);
            throw new InvalidIdException(message);
        }

        Optional<CourseDto> courseToBeUpdated = courseRepository.findById(id);
        
        if(courseToBeUpdated.isPresent()) {
            return courseRepository.save(course);
        } else {
            String message = "Course with id "+id+" is not present!";
            log.error(message);
            throw new ResourceNotFoundException(message);
        }
    }

}
