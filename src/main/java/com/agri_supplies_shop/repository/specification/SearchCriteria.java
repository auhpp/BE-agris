package com.agri_supplies_shop.repository.specification;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String nameObject;
    private String nameTableJoin;
    private String operation;
    private Object value;

}
