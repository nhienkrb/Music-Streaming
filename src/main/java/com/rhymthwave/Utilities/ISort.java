package com.rhymthwave.Utilities;

import org.springframework.data.domain.Sort;

public interface ISort<T,V> {
	
	Sort sortBy(T sortBy, V sortField);
	
	
}
