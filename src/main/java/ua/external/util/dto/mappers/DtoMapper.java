package ua.external.util.dto.mappers;

public interface DtoMapper<T, E> {

    T mapToDto(E e);

    E mapFromDto(T t);
}
