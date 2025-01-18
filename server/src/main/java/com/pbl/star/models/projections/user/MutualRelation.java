package com.pbl.star.models.projections.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MutualRelation {
    // Representative mutual relation
    // Will be displayed as "{repName} and {count - 1} others"
    private String repId;
    private String repName;

    private Integer count;
    private Integer score;
}
