package com.pbl.star.dtos.query.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MutualRelation {
    // Representative mutual relation
    // Will be displayed as "{repName} and {count - 1} others"
    private String repId;
    private String repName;

    private Integer count;
    private Integer score;
}
