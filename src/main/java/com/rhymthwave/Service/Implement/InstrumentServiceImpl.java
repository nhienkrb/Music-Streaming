package com.rhymthwave.Service.Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.InstrumentDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.ServiceAdmin.IInstrumentServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Instrument;
import com.rhymthwave.entity.Mood;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements CRUD<Instrument, Integer>{
	

	private final InstrumentDAO dao;
	

	@Override
	public Instrument create(Instrument entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	public Instrument update(Instrument entity) {
		if(entity!=null) {
			dao.save(entity);
			return entity;
		}
		return null;
	}

	@Override
	public Boolean delete(Integer key) {
		if(key instanceof Integer && key>0) {
			dao.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public Instrument findOne(Integer key) {
		if(key instanceof Integer && key>0) {
			return dao.findById(key).get();
		}
		return null;
	}

	@Override
	public List<Instrument> findAll() {
		return dao.findAll();
	}

}
