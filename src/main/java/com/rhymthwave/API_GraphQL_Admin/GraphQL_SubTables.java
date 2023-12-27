package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;

import com.rhymthwave.ServiceAdmin.*;
import com.rhymthwave.entity.*;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.CRUD;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GraphQL_SubTables {

	private final IMoodServiceAdmin mood;
	private final IInstrumentServiceAdmin instrument;
	private final ISongTypeServiceAdmin songType;
	private final ICultureServiceAdmin cultureServiceAdmin;
	private final IGenreService genreService;
	private  final ICountryServiceAdmin countryServiceAdmin;
	private final CRUD<Tag, Integer> tagService;

	@QueryMapping("getAllMood")
	public List<Mood> getAllMood() {
		return mood.findAllMood();
	}

	@QueryMapping("getAllInstrument")
	public List<Instrument> getAllInstrument() {
		return instrument.findAllInstrument();
	}

	@QueryMapping("getAllSongStyle")
	public List<SongStyle> getAllSongStyle() {
		return songType.findAllSongStyle();
	}

	@QueryMapping("getAllCulture")
	public List<Culture> getAllCulture() {
		return cultureServiceAdmin.findAllCulture();
	}

	@QueryMapping("getAllGener")
	public List<Genre> getAllGener() {
		return genreService.findAllGenre();
	}
	@QueryMapping("getAllCountry")
	public List<Country> getAllCountry() {
		return countryServiceAdmin.findAllCountry();
	}
	@QueryMapping("getAllTag")
	public List<Tag> getAllTag() {
		return tagService.findAll();
	}
}
