package ODL.web.authservice.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * Mapper interface
 *
 * @param <E> entity
 * @param <O> DTO
 */
public interface EntityToDTOMapper<E, O> {

    O toDTO(E entity, Object... args);

    E toEntity(O dto, Object... args);

    default List<O> toDTOs(List<E> entities, Object... args) {
        return entities.stream().map(entity -> toDTO(entity, args)).collect(Collectors.toList());
    }

    default List<E> toEntities(List<O> dtos, Object... args) {
        return dtos.stream().map(dto -> toEntity(dto, args)).collect(Collectors.toList());
    }
}
