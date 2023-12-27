package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Mood;

@Repository
public interface MoodDAO extends JpaRepository<Mood, Integer>{

	Mood findByMoodname(String moodname);

}
