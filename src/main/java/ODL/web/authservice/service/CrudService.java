package ODL.web.authservice.service;

import java.util.UUID;

import ODL.web.authservice.exception.BusinessEntityNotFoundException;

public interface CrudService<Entity, DTO> {

    /**
     * Create new entity
     * 
     * @param dto DTO
     * @return created entity (converted into DTO)
     */
    DTO create(DTO dto);

    /**
     * Find entity by ID
     * 
     * @param id entity ID
     * @return entity
     * @throws BusinessEntityNotFoundException if entity not found
     */
    DTO read(UUID id) throws BusinessEntityNotFoundException;

    DTO update(DTO dto);

    /**
     * Delete entity by id
     * 
     * @param id entity id
     * @throws BusinessEntityNotFoundException if entity not found
     */
    void delete(UUID id) throws BusinessEntityNotFoundException;
}