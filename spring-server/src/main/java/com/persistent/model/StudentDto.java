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
 * Student
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-11-22T15:31:36.117Z[UTC]")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

  @JsonProperty("id")
  @Id
  @GeneratedValue(generator = "student_id_seq", strategy = GenerationType.AUTO)
  @SequenceGenerator(name = "student_id_seq", sequenceName = "student_id_seq", initialValue = 1 ,allocationSize = 1)
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("address")
  private String address;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  
  public StudentDto(Student student){    
    ObjectMapper mapper = new ObjectMapper();
    try{    
        this.id = student.getId();
        this.name = student.getName();
        this.address = student.getAddress();
        this.email = student.getEmail();
        this.phone = student.getPhone();
    }catch (Exception e) {
        e.printStackTrace();
    }
  }

  public Student toEntity(){
    Student student = new Student();
    ObjectMapper mapper = new ObjectMapper();
    try{    
        student.setId(this.id);
        student.setName(this.name);
        student.setAddress(this.address);
        student.setEmail(this.email);
        student.setPhone(this.phone);
    }catch (Exception e) {
        e.printStackTrace();
    }
    return student;   
  }


}

