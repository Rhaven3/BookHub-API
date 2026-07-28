package fr.eni.td2j.bookhub_api.common;

public interface EntityMapper<ENTITY, DTO> {
    DTO toDto (ENTITY entity);
}
