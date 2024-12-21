package com.pbl.star.mapper;

public interface EntityMapper<D, E> {
    E toEntity(D dto);
}
