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

import com.rhymthwave.DAO.CultureDAO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.ServiceAdmin.ICultureServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Culture;
import com.rhymthwave.entity.Instrument;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CultureServiceAdminImp implements ICultureServiceAdmin {

	private final CultureDAO cultureDAO;

	private final ISort<String, String> sortService;

	private final AccountService accountService;

	@Override
	public Page<Culture> getCulturePage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 6, sort);

			Page<Culture> pageMood = cultureDAO.findAll(pageable);
			return pageMood;
		} catch (Exception e) {
			return null;
		}

	}

	

	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}



	@Override
	public Culture findById(Integer idSongStyle) {
		Optional<Culture> SongStyle = cultureDAO.findById(idSongStyle);
		if (SongStyle.isEmpty()) {
			return null;
		}

		return SongStyle.get();
	}



	@Override
	public Culture create(Culture Culture, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);
		if (cultureDAO.findByCultureName(Culture.getCultureName()) != null) {
			return null;
		} 
		else if (account == null || account.equals("") || account.equals(null)) {
			return null;
		}
		Culture.setCreateBy(account.getEmail());
		Culture.setCreateDate(getTimeNow());

		return cultureDAO.save(Culture);
	}



	@Override
	public Culture update(Culture entity, HttpServletRequest request) {
		Account account = accountService.findAdminByEmail(request);

		Optional<Culture> Culture = cultureDAO.findById(entity.getCultureId());

		if (Culture.isEmpty()) {
			return null;
		} else if (account == null) {
			return null;
		}
		
		entity.setModifiedBy(account.getEmail());
		entity.setModifiDate(getTimeNow());
		return cultureDAO.save(entity);
	}



	@Override
	public boolean delete(Integer id) {
		Culture SongStyle = findById(id);
		if (SongStyle == null) {
			return false;
		}
		cultureDAO.delete(SongStyle);
		return true;
	}



	@Override
	public List<Culture> findAllCulture() {
		return cultureDAO.findAll();
	}




}
