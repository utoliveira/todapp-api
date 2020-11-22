package com.atlas.todappapi.bean.mapper;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface IMapper<E,D> {

    D toDTO(E entity);

    default Collection<D> toCollectionDTO(Collection<E> entities){
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(entity -> toDTO(entity)).collect(Collectors.toList());
    }

}
