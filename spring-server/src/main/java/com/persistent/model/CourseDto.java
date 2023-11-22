package com.persistent.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
/**
 * Course
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-22T15:31:36.117Z[UTC]")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

  @JsonProperty("id")
  @Id
  @GeneratedValue(generator = "course_id_seq", strategy = GenerationType.AUTO)
  @SequenceGenerator(name = "course_id_seq", sequenceName = "course_id_seq", initialValue = 1 ,allocationSize = 1)
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("desc")
  private String desc;

  
  public CourseDto(Course course){    
    ObjectMapper mapper = new ObjectMapper();
    try{    
        this.id = course.getId();
        this.name = course.getName();
        this.desc = course.getDesc();
    }catch (Exception e) {
        e.printStackTrace();
    }
  }

  public Course toEntity(){
    Course course = new Course();
    ObjectMapper mapper = new ObjectMapper();
    try{    
        course.setId(this.id);
        course.setName(this.name);
        course.setDesc(this.desc);
    }catch (Exception e) {
        e.printStackTrace();
    }
    return course;   
  }


}

