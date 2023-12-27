package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.Genre;

public interface IGenreService {

	Page<Genre> getGenrePage(Integer page, String sortBy, String sortField);

	List<Genre> findAllGenre();
}
