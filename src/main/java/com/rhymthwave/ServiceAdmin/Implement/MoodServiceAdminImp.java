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

import com.rhymthwave.DAO.MoodDAO;
import com.rhymthwave.Service.AccountService;
import com.rhymthwave.ServiceAdmin.IMoodServiceAdmin;
import com.rhymthwave.Utilities.ISort;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Mood;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoodServiceAdminImp implements IMoodServiceAdmin {

	private final MoodDAO moodDAO;

	private final ISort<String, String> sortService;

	private final AccountService accountService;

	@Override
	public Page<Mood> getMoodPage(Integer page, String sortBy, String sortField) {

		try {
			Sort sort = sortService.sortBy(sortBy, sortField);

			Pageable pageable = PageRequest.of(page, 12, sort);

			Page<Mood> pageMood = moodDAO.findAll(pageable);
			return pageMood;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Mood create(Mood entity, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);
		System.out.println("email: " + account.getEmail());
		if (moodDAO.findByMoodname(entity.getMoodname()) != null) {
			return null;
		} else if (account == null || account.equals("") || account.equals(null)) {
			return null;
		}
		entity.setCreateBy(account.getEmail());
		entity.setCreateDate(getTimeNow());

		return moodDAO.save(entity);
	}

	@Override
	public Mood update(Mood entity, HttpServletRequest request) {

		Account account = accountService.findAdminByEmail(request);

		Optional<Mood> mood = moodDAO.findById(entity.getMoodid());

		if (mood.isEmpty()) {
			return null;
		}
		else if (account == null) {
			return null;
		}

		entity.setModifiedBy(account.getEmail());
		entity.setModifiDate(getTimeNow());
		return moodDAO.save(entity);
	}

	@Override
	public boolean delete(Integer key) {
		Mood mood = findById(key);
		if (mood == null) {
			return false;
		}
		moodDAO.delete(mood);
		return true;
	}

	@Override
	public Mood findById(Integer key) {
		Optional<Mood> mood = moodDAO.findById(key);
		if (mood.isEmpty()) {
			return null;
		}

		return mood.get();
	}

	@Override
	public List<Mood> findAllMood() {

		return moodDAO.findAll();
	}

	public Date getTimeNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		return new Date(calendar.getTime().getTime());
	}

}
