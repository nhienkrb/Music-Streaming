package com.rhymthwave.Service.Implement;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rhymthwave.DAO.MoodDAO;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Mood;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoodServiceImpl implements CRUD<Mood, Integer>{

	
	private final MoodDAO moodDAO;
	
	@Override
	public Mood create(Mood entity) {
		
		if(moodDAO.findByMoodname(entity.getMoodname()) !=null) {
			return null;
		}
		
		
		return moodDAO.save(entity);
	}

	@Override
	public Mood update(Mood entity) {
		
		Mood mood = findOne(entity.getMoodid());
		if(mood == null) {
			return null;
		}
		return moodDAO.save(entity);
	}

	@Override
	public Boolean delete(Integer key) {
		Mood mood = findOne(key);
		if(mood == null) {
			return false;
		}
		moodDAO.delete(mood);
		return true;
	}

	@Override
	public Mood findOne(Integer key) {
		Optional<Mood> mood = moodDAO.findById(key);
		if(mood.isEmpty()) {
			return null;
		}
		
		return mood.get();
	}

	@Override
	public List<Mood> findAll() {
		
		return moodDAO.findAll();
	}

}
