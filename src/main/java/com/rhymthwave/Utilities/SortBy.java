package com.rhymthwave.Utilities;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class SortBy<T,V> implements ISort<T, V>{

	@Override
	public Sort sortBy(T sortBy, V sortField) {
		Sort sort = (sortBy == null || sortBy.equals("asc")) ? Sort.by(Direction.ASC, (String)sortField)
				: Sort.by(Direction.DESC, (String)sortField);
		return sort;
	}


}
