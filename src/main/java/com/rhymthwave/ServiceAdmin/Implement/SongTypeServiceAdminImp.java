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

import com.rhymthwave.DAO.SongStyleDAO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.ServiceAdmin.ISongTypeServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Culture;
import com.rhymthwave.entity.SongStyle;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongTypeServiceAdminImp implements ISongTypeServiceAdmin {

	private final SongStyleDAO songStyleDAO;

	private final ISort<String, String> sortService;

	private final AccountService accountService;

	@Override
	public Page<SongStyle> getSongTypePage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 6, sort);

			Page<SongStyle> pageMood = songStyleDAO.findAll(pageable);
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
	public SongStyle findById(Integer idSongStyle) {
		Optional<SongStyle> SongStyle = songStyleDAO.findById(idSongStyle);
		if (SongStyle.isEmpty()) {
			return null;
		}

		return SongStyle.get();
	}



	@Override
	public SongStyle create(SongStyle SongStyle, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);
		if (songStyleDAO.findBySongStyleName(SongStyle.getSongStyleName()) != null) {
			return null;
		} 
		else if (account == null || account.equals("") || account.equals(null)) {
			return null;
		}
		SongStyle.setCreateBy(account.getEmail());
		SongStyle.setCreateDate(getTimeNow());

		return songStyleDAO.save(SongStyle);
	}



	@Override
	public SongStyle update(SongStyle entity, HttpServletRequest request) {
		Account account = accountService.findAdminByEmail(request);

		Optional<SongStyle> SongStyle = songStyleDAO.findById(entity.getSongStyleId());

		if (SongStyle.isEmpty()) {
			return null;
		} else if (account == null) {
			return null;
		}
		
		entity.setModifiedBy(account.getEmail());
		entity.setModifiDate(getTimeNow());
		return songStyleDAO.save(entity);
	}



	@Override
	public boolean delete(Integer idSongType) {
		SongStyle SongStyle = findById(idSongType);
		if (SongStyle == null) {
			return false;
		}
		songStyleDAO.delete(SongStyle);
		return true;
	}



	@Override
	public List<SongStyle> findAllSongStyle() {
		return songStyleDAO.findAll();
	}




}
