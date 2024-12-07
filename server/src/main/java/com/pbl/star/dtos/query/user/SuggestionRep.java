package com.pbl.star.dtos.query.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuggestionRep {
    private String id;
    private String repId;
    private String repName;
}
