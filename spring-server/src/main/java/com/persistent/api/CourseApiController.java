/**
 * Controller class for handling CRUD operations on Course resource.
 */
package com.persistent.api;
import com.persistent.model.Course;

import com.persistent.model.Course;
import com.persistent.model.CourseDto;
import com.persistent.service.CourseService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;
import com.persistent.util.Constants;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-22T15:31:36.117Z[UTC]")
@Controller
@RequestMapping("${openapi.swaggerStudentManagementSystemOpenAPI30.base-path:/api/v3}")
public class CourseApiController implements CourseApi {

    private final NativeWebRequest request;

    /**
     * Service instance for database operations.
     */
    @Autowired
    private CourseService courseService;

    @Autowired
    public CourseApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    /**
     * Services the addCourse API POST call.
     * @return The Course object with the API call response and appropriate HTTP 
     *         Status code.
     */
    @Override
    public ResponseEntity<Course> addCourse(@Parameter(name = "Course", description = "Create a new course", required = true) @Valid @RequestBody Course course
    ){
        CourseDto coursedto = new CourseDto(course);
        CourseDto processedcourse = courseService.addCourse(coursedto);  
        return new ResponseEntity<Course>(processedcourse.toEntity(),HttpStatus.OK);
    }
    /**
     * Services the deleteCourse API DELETE call.
     * @return The Course object with the API call response and appropriate HTTP 
     *         Status code.
     */
    @Override
    public ResponseEntity<Course> deleteCourse(@Parameter(name = "courseId", description = "Course id to delete", required = true, in = ParameterIn.PATH) @PathVariable("courseId") Long courseId
    ){
        CourseDto processedcourse = courseService.deleteCourse(courseId);  
        return new ResponseEntity<Course>(processedcourse.toEntity(),HttpStatus.OK);
    }
    /**
     * Services the getCourse API GET call.
     * @return The Course object with the API call response and appropriate HTTP 
     *         Status code.
     */
    @Override
    public ResponseEntity<Course> getCourse(@Parameter(name = "courseId", description = "ID of course to return", required = true, in = ParameterIn.PATH) @PathVariable("courseId") Long courseId
    ){
        CourseDto processedcourse = courseService.getCourse(courseId);  
        return new ResponseEntity<Course>(processedcourse.toEntity(),HttpStatus.OK);
    }
    /**
     * Services the getCourseList API GET call.
     * @return The Course object with the API call response and appropriate HTTP 
     *         Status code.
     */
    @Override
    public ResponseEntity<List<Course>> getCourseList(
        @RequestParam(value = "query_string", defaultValue = Constants.DEFAULT_SEARCH_CRITERIA, required = false) String queryString,
        @RequestParam(value = "pageNumber", defaultValue = Constants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(value = "pageSize", defaultValue = Constants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(value = "sortBy", defaultValue = Constants.DEFAULT_SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = Constants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        List<CourseDto> courseDtoList = courseService.getCourseList(queryString,pageNumber,pageSize,sortBy,sortDir);
        List<Course> courseList = new ArrayList<Course>();
        for(CourseDto courseDto : courseDtoList){
            courseList.add(courseDto.toEntity());
        }
        return new ResponseEntity<List<Course>>(courseList, HttpStatus.OK);
    }
    /**
     * Services the updateCourse API PUT call.
     * @return The Course object with the API call response and appropriate HTTP 
     *         Status code.
     */
    @Override
    public ResponseEntity<Course> updateCourse(@Parameter(name = "Course", description = "Update an existent course", required = true) @Valid @RequestBody Course course
    ){
        CourseDto coursedto = new CourseDto(course);
        CourseDto processedcourse = courseService.updateCourse(coursedto);  
        return new ResponseEntity<Course>(processedcourse.toEntity(),HttpStatus.OK);
    }
}
