package com.rhymthwave.ServiceAdmin.Implement;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.InstrumentDAO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.ServiceAdmin.IInstrumentServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Instrument;
import com.rhymthwave.entity.Mood;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstrumentServiceAdminImp implements IInstrumentServiceAdmin {

	private final InstrumentDAO instrumentDAO;

	private final ISort<String, String> sortService;

	private final AccountService accountService;

	@Override
	public Page<Instrument> getInstrumentPage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 6, sort);

			Page<Instrument> pageMood = instrumentDAO.findAll(pageable);
			return pageMood;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Instrument create(Instrument entity, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);
		System.out.println("email: "+account.getEmail());
		if (instrumentDAO.findByInstrumentName(entity.getInstrumentName()) != null) {
			return null;
		} 
		else if (account == null || account.equals("") || account.equals(null)) {
			return null;
		}
		entity.setCreateBy(account.getEmail());
		entity.setCreateDate(getTimeNow());

		return instrumentDAO.save(entity);
	}

	@Override
	public Instrument update(Instrument entity, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);

		Optional<Instrument> Instrument = instrumentDAO.findById(entity.getInstrumentId());

		if (Instrument.isEmpty()) {
			return null;
		} else if (account == null) {
			return null;
		}
		
		entity.setModifiedBy(account.getEmail());
		entity.setModifiDate(getTimeNow());
		return instrumentDAO.save(entity);
	}

	@Override
	public boolean delete(Integer key) {
		Instrument Instrument = findById(key);
		if (Instrument == null) {
			return false;
		}
		instrumentDAO.delete(Instrument);
		return true;
	}

	@Override
	public Instrument findById(Integer key) {
		Optional<Instrument> Instrument = instrumentDAO.findById(key);
		if (Instrument.isEmpty()) {
			return null;
		}

		return Instrument.get();
	}

	@Override
	public List<Instrument> findAllInstrument() {

		return instrumentDAO.findAll();
	}

	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}

}
