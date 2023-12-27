package com.rhymthwave.ServiceAdmin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rhymthwave.entity.Instrument;

import jakarta.servlet.http.HttpServletRequest;

public interface IInstrumentServiceAdmin {
	Instrument findById(Integer idInstrument);

	Instrument create(Instrument instrument, HttpServletRequest request); 
	
	Instrument update(Instrument instrument, HttpServletRequest request); 
	
	boolean delete(Integer idInstrument );
	
	List<Instrument> findAllInstrument();
	
	Page<Instrument> getInstrumentPage(Integer page, String sortBy, String sortField);
}
