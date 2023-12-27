package com.rhymthwave.Utilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rhymthwave.DAO.CultureDAO;
import com.rhymthwave.DAO.GenreDAO;
import com.rhymthwave.DAO.InstrumentDAO;
import com.rhymthwave.DAO.MoodDAO;
import com.rhymthwave.DAO.SongStyleDAO;
import com.rhymthwave.entity.Culture;
import com.rhymthwave.entity.Genre;
import com.rhymthwave.entity.Instrument;
import com.rhymthwave.entity.Mood;
import com.rhymthwave.entity.SongStyle;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportEx {
	
	
	private final SongStyleDAO dao;
	
	private final MoodDAO moodDao;
	
	@Autowired
	Excel excel;
	
	public void save (MultipartFile multipartFile) {
		try {
			List<SongStyle> instruments = excel.convertExceltoInstrument(multipartFile.getInputStream());
				System.out.println(instruments.isEmpty());
				dao.saveAll(instruments);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
