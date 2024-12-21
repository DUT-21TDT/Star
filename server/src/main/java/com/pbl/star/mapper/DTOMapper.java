package com.pbl.star.mapper;

import java.util.Collection;

public interface DTOMapper<D, E> {
    D toDTO(E entity);
    Collection<D> toDTO(Collection<E> entities);
}
