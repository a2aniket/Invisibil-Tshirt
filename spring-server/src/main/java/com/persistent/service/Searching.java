package com.persistent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.persistent.model.SearchString;
import com.persistent.util.Constants;


@Log4j2
public class Searching {

    /**
     * This is method is used to count the number of DELIMITERs present in the queryString.
     * Assuming DELIMITER as ';'.
     * For e.g for queryString id <= 10; name = "John Doe" this will return count of DELIMITERs as 1.
     * @param input String containing the DELIMITER.
     * @return The total count of DELIMITER.
     */
    public static int countDELIMITER(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        String[] substrings = input.split(";");
        return substrings.length - 1;
    }
    
    /**
     * This method is used to break queryString into column,operator,value.
     * @param partOfSearchString The single part of query string.
     * @return The object of SearchString class.
     */
    public static SearchString getCriteriaObject(String partOfSearchString){

        int i=0;
        int currentIndex = 0;
        SearchString searchString = new SearchString();

        partOfSearchString = partOfSearchString.trim();
        partOfSearchString = partOfSearchString.replace("\"", "");
        currentIndex = partOfSearchString.indexOf(' ');

        // If query string contains % then we need to find its position(before or after search value) 
        // in the query string in order to place % appropriately in the pedicates. 
        if(partOfSearchString.contains("%")){
            
            int indexOfPercentage = partOfSearchString.indexOf('%');

            // Example values query string 
            // Column : name 
            // Operator :LIKE 
            // Value : John Doe
            // If % is before and after value string e.g %hn Do%
            if(
                (partOfSearchString.charAt((indexOfPercentage-1)) == ' ' ) &&
                ((indexOfPercentage+1) == (partOfSearchString.length()))
            ){
                searchString.setPercentageBeforeValue(true);
                searchString.setPercentageAfterValue(true);
            }
            // If % is before value e.g. %hn Doe
            else if(
                partOfSearchString.charAt((indexOfPercentage-1)) == ' '  
            ){
                searchString.setPercentageBeforeValue(true);
            }
            // If % is the last charecter of value string e.g John D%
            else if(
                (indexOfPercentage+1) == (partOfSearchString.length())
            ){
                searchString.setPercentageAfterValue(true);
            }
        }
        // Extract the column name using substring.
        searchString.setColumn(partOfSearchString.substring(i, currentIndex));

        i = currentIndex+1;
        currentIndex = partOfSearchString.indexOf(' ',currentIndex+1);

        // If operator is ISNULL or ISNOTNULL then partOfSearchString will only have column name and operator.
        if(partOfSearchString.contains("ISNULL") || partOfSearchString.contains("ISNOTNULL")){
            // Hence extract the substring by using length as ending index.
            searchString.setOperator(partOfSearchString.substring(i, (partOfSearchString.length())));
        }else{
            // Extract the operator using substring.
            searchString.setOperator(partOfSearchString.substring(i, currentIndex));

            // Extract the value using substring.
            i = currentIndex+1;
            currentIndex = partOfSearchString.indexOf(' ',currentIndex+1);
            searchString.setValue(partOfSearchString.substring(i, (partOfSearchString.length())));
        }
        return searchString;
    }

    /**
     * This method will break multiple search string into individual parts based on the DELIMITER.
     * For e.g. queryString = name LIKE "%25hn Do%25";id <= 10; age > 20
     * This method will break above queryString like
     * Part 1 : name LIKE %hn Do%
     * Part 2 : id <= 10
     * Part 3 : age > 20
     * @param queryString The query string entered by user.
     * @return The list of individual parts of query string.
     */
    public static List<SearchString> getSearchStringList(String queryString){
        List<SearchString> searchString = new ArrayList<>();
        int numberOfSemicolons = countDELIMITER(queryString);
        log.info("Count of semicolons : "+numberOfSemicolons);
        int i=0;
        int currentIndex = 0;
        List<String> partOfSearchString = new ArrayList<>();
        while(numberOfSemicolons>0){

            if(currentIndex == 0)
                currentIndex = queryString.indexOf(";");
            else
                currentIndex = queryString.indexOf(";", currentIndex+1 );

            partOfSearchString.add(queryString.substring(i, currentIndex));
            i = currentIndex+1;

            numberOfSemicolons--;
        }
        partOfSearchString.add(queryString.substring(i, (queryString.length())));

        for(String str : partOfSearchString){
            searchString.add(getCriteriaObject(str));
        }
        return searchString;
    }

    /**
     * This method is used to filter the values of IN clause string.
     * @param inClause The IN clause string.
     * @return The list of values of IN clause.
     */
    public static List<Object> filterInClause(String inClause){
        List<Object> inputList = Arrays.asList(inClause.substring(1, inClause.length() - 1).split(","))
                                .stream()
                                .map(s -> {
                                    if (s.startsWith("'") && s.endsWith("'")) {
                                        return s.substring(1, s.length() - 1);
                                    } else {
                                        return Long.parseLong(s.trim());
                                    }
                                })
                            .collect(Collectors.toList());
        return inputList;
    }

    /**
     * This method is used to build/generate the list of predicates.
     * @param resultType The class for which the criteria
     * @param queryString The query entered by user.
     * @param entityManager 
     * @param criteriaBuilder 
     * @param sortBy The column name to sort the list by.
     * @param sortDir The sort direction i.e. ASC or DESC.
     * @param pageable
     * @return The retrived list from the database.
     */
    public static <T> List<T> generatePredicateList(Class<T> resultType,String queryString, EntityManager entityManager,CriteriaBuilder criteriaBuilder,String sortBy, String sortDir, Pageable pageable ){

        List<SearchString> searchString = null;
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(resultType);
        String betweenClauseValue = "";
        int indexOfAnd = -1, firstNumber = -1, secondNumber = -1;
        String delimiterString = "AND";
        String notBetweenClauseValue = "";
        List<Object> inputList = null;
        String inClauseValue = "";
        String notInClauseValue = "";
        List<Predicate> predicateList = new ArrayList<>();
        int i=0;

        // select * from table where
        Root<T> root = criteriaQuery.from(resultType);

        // Extract the individual parts of queryString and store them in searchString list.
        searchString = getSearchStringList(queryString);

        // Setting predicates based on the operator by iterating over the searchString list. 
        while(i<searchString.size()){
            switch(searchString.get(i).getOperator()){
                case "=" :          predicateList.add(criteriaBuilder.equal(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case "<" :          predicateList.add(criteriaBuilder.lessThan(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case ">" :          predicateList.add(criteriaBuilder.greaterThan(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case "!=" :         predicateList.add(criteriaBuilder.notEqual(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case "<>" :         predicateList.add(criteriaBuilder.notEqual(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case "<=" :         predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                case ">=" :         predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue()));
                                    break;
                // case-sensetive
                case "LIKE" :       if( (searchString.get(i).isPercentageBeforeValue() == true) && (searchString.get(i).isPercentageAfterValue() == true)){
                                        predicateList.add(criteriaBuilder.like(root.get(searchString.get(i).getColumn()), "%" + searchString.get(i).getValue() + "%"));
                                    }else if(searchString.get(i).isPercentageBeforeValue() == true){
                                        predicateList.add(criteriaBuilder.like(root.get(searchString.get(i).getColumn()), "%" + searchString.get(i).getValue()));
                                    }else if(searchString.get(i).isPercentageAfterValue() == true){
                                        predicateList.add(criteriaBuilder.like(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue() + "%"));
                                    }
                                    break;
                case "NOTLIKE" :    if( (searchString.get(i).isPercentageBeforeValue() == true) && (searchString.get(i).isPercentageAfterValue() == true)){
                                        predicateList.add(criteriaBuilder.notLike(root.get(searchString.get(i).getColumn()), "%" + searchString.get(i).getValue() + "%"));
                                    }else if(searchString.get(i).isPercentageBeforeValue() == true){
                                        predicateList.add(criteriaBuilder.notLike(root.get(searchString.get(i).getColumn()), "%" + searchString.get(i).getValue()));
                                    }else if(searchString.get(i).isPercentageAfterValue() == true){
                                        predicateList.add(criteriaBuilder.notLike(root.get(searchString.get(i).getColumn()), searchString.get(i).getValue() + "%"));
                                    }
                                    break;
                case "BETWEEN" :    betweenClauseValue = searchString.get(i).getValue();
                                    indexOfAnd = -1;
                                    firstNumber = -1;
                                    secondNumber = -1;
                                    indexOfAnd = betweenClauseValue.indexOf(delimiterString);
                                    
                                    firstNumber = Integer.parseInt((betweenClauseValue.substring(0, indexOfAnd)).trim());
                                    secondNumber = Integer.parseInt((betweenClauseValue.substring(betweenClauseValue.indexOf(' ',indexOfAnd+1),betweenClauseValue.length())).trim());

                                    predicateList.add(criteriaBuilder.between(root.get(searchString.get(i).getColumn()), firstNumber, secondNumber));
                                    break;
                case "NOTBETWEEN" : notBetweenClauseValue = searchString.get(i).getValue();
                                    indexOfAnd = -1;
                                    firstNumber = -1;
                                    secondNumber = -1;
                                    delimiterString = "AND";
                                    indexOfAnd = notBetweenClauseValue.indexOf(delimiterString);
                                    
                                    firstNumber = Integer.parseInt((notBetweenClauseValue.substring(0, indexOfAnd)).trim());
                                    secondNumber = Integer.parseInt((notBetweenClauseValue.substring(notBetweenClauseValue.indexOf(' ',indexOfAnd+1),notBetweenClauseValue.length())).trim());

                                    predicateList.add(criteriaBuilder.between(root.get(searchString.get(i).getColumn()), firstNumber, secondNumber).not());
                                    break;
                case "IN" :         inClauseValue = searchString.get(i).getValue();
                                    inputList = Searching.filterInClause(inClauseValue);
                                    predicateList.add(criteriaBuilder.in(root.get(searchString.get(i).getColumn())).value(inputList));
                                    break;
                case "NOTIN" :      notInClauseValue = searchString.get(i).getValue();
                                    inputList = Searching.filterInClause(notInClauseValue);
                                    predicateList.add(criteriaBuilder.in(root.get(searchString.get(i).getColumn())).value(inputList).not());
                                    break;
                case "ISNULL" :     predicateList.add(criteriaBuilder.isNull(root.get(searchString.get(i).getColumn())));
                                    break;
                case "ISNOTNULL" :  predicateList.add(criteriaBuilder.isNotNull(root.get(searchString.get(i).getColumn())));
                                    break;
                default :           break; 
            }
            i++;
        }
        // Building WHERE clause by using 'AND' operator.
        criteriaQuery.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()])));
        
        // Setting ORDER BY clause for Sorting.
        if(sortDir.equalsIgnoreCase(Constants.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy))); 
        }else{
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy))); 
        }
        return entityManager.createQuery(criteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }
}
