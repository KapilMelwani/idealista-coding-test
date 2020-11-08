package com.idealista.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapper {

	private static ModelMapper modelMapper = new ModelMapper();

	/**
	 * Model mapper property setting are specified in the following block. Default property matching
	 * strategy is set to Strict see {@link MatchingStrategies} Custom mappings are added using {@link
	 * ModelMapper#addMappings(PropertyMap)}
	 */
	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	/** Hide from public usage. */
	private ObjectMapper() {}

	/**
	 * Note: outClass object must have default constructor with no arguments
	 *
	 * @param <D> type of result object.
	 * @param <T> type of source object to map from.
	 * @param entity entity that needs to be mapped.
	 * @param outClass class of result object.
	 * @return new object of <code>outClass</code> type.
	 */
	public static <D, T> D map(final T entity, final Class<D> outClass) {
		return modelMapper.map(entity, outClass);
	}

	/**
	 * Note: outClass object must have default constructor with no arguments
	 *
	 * @param entityList list of entities that needs to be mapped
	 * @param outCLass class of result list element
	 * @param <D> type of objects in result list
	 * @param <T> type of entity in <code>entityList</code>
	 * @return list of mapped object with <code><D></code> type.
	 */
	public static <D, T> List<D> mapAll(final Collection<T> entityList, final Class<D> outCLass) {
		return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
	}

	/**
	 * Maps {@code source} to {@code destination}.
	 *
	 * @param source object to map from
	 * @param destination object to map to
	 */
	public static <S, D> D map(final S source, final D destination) {
		modelMapper.map(source, destination);
		return destination;
	}
}
