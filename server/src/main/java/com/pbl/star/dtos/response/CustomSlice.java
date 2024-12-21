package com.pbl.star.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomSlice<T> extends SliceImpl<T> {
    private Integer totalElements;

    public CustomSlice(Slice<T> slice) {
        super(slice.getContent(), slice.getPageable(), slice.hasNext());
    }

    @Override
    @NonNull
    public <U> CustomSlice<U> map(@NonNull Function<? super T, ? extends U> converter) {
        List<U> convertedContent = this.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());

        return new CustomSlice<>(new SliceImpl<>(
                convertedContent,
                this.getPageable(),
                this.hasNext()
        ));
    }
}
