package com.persistent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchString{
    private String column;
    private String operator;
    private String value;
    private boolean percentageBeforeValue = false;
    private boolean percentageAfterValue = false;
}