package com.persistent.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.persistent.util.Constants;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;

@Log4j2
public final class PaginationAndSorting {
 
    private static String message = "";
    private static Sort sort = null;
    
    /**
     * This method is used to check if value is invalid.
     * @param value The value that needs to be checked.
     * @param defaultValue The default value.
     * @param message The warning message that needs to be printed.
     * @return Verified value.
     */
    public static int check(int value, int defaultValue, String message){
        if(value < 1){
            log.warn(message);
            value = defaultValue;
        }
        return value;
    }
    
    /**
     * This method is used to check whether the page number is valid and if not valid 
     * then it will revert it to its default value.
     * @param pageNumber The pageNumber entered by user.
     * @return The valid pageNumber.
     */
    public static int checkPageNumber(int pageNumber){
        message = "Invalid page number detected! " + pageNumber + " Correcting it to default value i.e. 1.";
        pageNumber = check(pageNumber, Integer.parseInt(Constants.DEFAULT_PAGE_NUMBER),message);
        return pageNumber;
    }

    /**
     * This method is used to check whether the page size is valid and if not valid 
     * then it will revert it to its default value.
     * @param pageSize The page size to check.
     * @return The valid pageSize.
     */
    public static int checkPageSize(int pageSize){
        message = "Invalid page size detected! " + pageSize + " Correcting it to default value i.e. 10.";
        pageSize = check(pageSize, Integer.parseInt(Constants.DEFAULT_PAGE_SIZE),message);
        return pageSize;
    }

    /**
     * This method is used to check the sort by attribute and if invalid value is 
     * provided then it will revert it to its default value.
     * @param <T>  The type parameter of the class.
     * @param resultType The class object representing the class type.
     * @param sortBy The column name to check.
     * @return The valid column name for the given class type.
     */
    public static <T> String checkColumnName(Class<T> classType,String sortBy){
        Class<T> dtoClass = classType;
        List<String> attributeNames = new ArrayList<>();
        // Using reflection to get the list of attributes.
        for (Field field : dtoClass.getDeclaredFields()) {
            attributeNames.add(field.getName());
        }

        // Checking if the provided sort by attribute is valid.
        if(!attributeNames.contains(sortBy)){
            message = "Invalid sort by attribute detected! " + sortBy + "  Correcting it to default value i.e. id.";
            log.warn(message);
            sortBy = Constants.DEFAULT_SORT_BY;
        }
        return sortBy;
    }

    /**
     * This method is used to check the sorting direction and if invalid value is 
     * provided then it will revert it to its default value.
     * @param sortDir The sorting direction to check.
     * @return The valid sorting direction.
     */
    public static String checkSortDirection(String sortDir){
        if (sortDir.equalsIgnoreCase(Constants.ASC)) {
            sortDir = Constants.ASC;
        } else if (sortDir.equalsIgnoreCase(Constants.DESC)) {
            sortDir = Constants.DESC;
        }
        else{
            message = "Invalid sort direction detected! " + sortDir + "  Correcting it to default value i.e. ASC";
            log.warn(message);
            sortDir = Constants.ASC;
        }
        return sortDir;
    }

    /**
     * This method is used to set the Sort object using sortBy and sortDir.
     * @param sortBy The column name.
     * @param sortDir The sorting direction.
     * @return Created Sort object.
     */
    public static Sort setSortObject(String sortBy, String sortDir){
        if(sortDir.equals(Constants.ASC))
            sort = Sort.by(sortBy).ascending();
        else
        sort = Sort.by(sortBy).descending();
        return sort;
    }
}
