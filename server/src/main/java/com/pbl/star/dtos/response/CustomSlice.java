package com.pbl.star.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@Getter
@Setter
public class CustomSlice<T> extends SliceImpl<T> {
    public CustomSlice(Slice<T> slice) {
        super(slice.getContent(), slice.getPageable(), slice.hasNext());
    }

    private Integer totalElements;
}
