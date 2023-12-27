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

import com.rhymthwave.DAO.CountryDAO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.ServiceAdmin.ICountryServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Country;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryServiceAdminImp implements ICountryServiceAdmin {

	private final CountryDAO countryDAO;

	private final ISort<String, String> sortService;

	private final AccountService accountService;

	@Override
	public Page<Country> getCountryPage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 6, sort);

			Page<Country> pageMood = countryDAO.findAll(pageable);
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
	public Country findById(String idCountry) {
		Optional<Country> country = countryDAO.findById(idCountry);
		if (country.isEmpty()) {
			return null;
		}

		return country.get();
	}

	@Override
	public Country create(Country Country, HttpServletRequest request) {
		Account account = accountService.findAdminByEmail(request);
		if (countryDAO.findByNameCountry(Country.getNameCountry()) != null) {
			return null;
		} else if (account == null || account.equals("") || account.equals(null)) {
			return null;
		}
		Country.setCreateBy(account.getEmail());
		Country.setCreateDate(getTimeNow());

		return countryDAO.save(Country);
	}

	@Override
	public Country update(Country entity, HttpServletRequest request) {
		Account account = accountService.findAdminByEmail(request);

		Optional<Country> country = countryDAO.findById(entity.getId());

		if (country.isEmpty()) {
			return null;
		} 
		else if (account == null) {
			return null;
		}

		entity.setModifiedBy(account.getEmail());
		entity.setModifiDate(getTimeNow());
		return countryDAO.save(entity);
	}

	@Override
	public boolean delete(String idCountry) {
		Country Country = findById(idCountry);
		if (Country == null) {
			return false;
		}
		countryDAO.delete(Country);
		return true;
	}

	@Override
	public List<Country> findAllCountry() {
		return countryDAO.findAll();
	}

}
