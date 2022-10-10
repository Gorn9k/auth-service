package ODL.web.authservice.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ODL.web.authservice.entity.AbstractEntity;
import ODL.web.authservice.exception.BusinessEntityNotFoundException;
import ODL.web.authservice.service.mapper.EntityToDTOMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@Lazy
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CrudImpl<Entity extends AbstractEntity, Repository extends JpaRepository<Entity, UUID>, DTO, DTOMapper extends EntityToDTOMapper<Entity, DTO>>
        implements CrudService<Entity, DTO> {

    @Autowired
    Repository repository;

    @Autowired
    DTOMapper mapper;

    /**
     * Get entity type name
     * 
     * @return entity type name
     */
    private String getEntityTypeName() {
        return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
    }

    /**
     * Get entity by id
     * 
     * @param id entity id
     * @return entity
     * @throws BusinessEntityNotFoundException if entity not found
     */
    @Transactional(readOnly = true)
    public Entity get(UUID id) throws BusinessEntityNotFoundException {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessEntityNotFoundException(getEntityTypeName() + " not found"));
    }

    /**
     * Get all entities
     * 
     * @return entities array
     */
    @Transactional(readOnly = true)
    public List<DTO> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Override
    @Transactional
    public DTO create(DTO dto) {
        Entity entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public DTO read(UUID id) throws BusinessEntityNotFoundException {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new BusinessEntityNotFoundException(getEntityTypeName() + " not found")));
    }

    /**
     * TODO: rewrite it
     */
    @Override
    @Transactional
    public DTO update(DTO dto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(UUID id) throws BusinessEntityNotFoundException {
        repository.findById(id)
                .orElseThrow(() -> new BusinessEntityNotFoundException(getEntityTypeName() + " not found"));
        repository.deleteById(id);
    }

}