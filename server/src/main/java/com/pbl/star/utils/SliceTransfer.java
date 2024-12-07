package com.pbl.star.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class SliceTransfer {
    public static <T> Slice<T> trimToSlice(List<T> list, int limit) {
        boolean hasNext = false;
        if (list.size() > limit) {
            list = list.subList(0, limit);
            hasNext = true;
        }
        Pageable pageable = PageRequest.of(0, limit);
        return new SliceImpl<>(list, pageable, hasNext);
    }
}
