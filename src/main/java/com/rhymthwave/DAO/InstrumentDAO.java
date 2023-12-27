package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Instrument;

@Repository
public interface InstrumentDAO extends JpaRepository<Instrument, Integer>{

	Instrument findByInstrumentName(String instrumentName);
}
