package com.rhymthwave.ServiceAdmin;

import com.rhymthwave.DAO.*;
import com.rhymthwave.DTO.NumberCreateRecordAndEpisodeByDate;
import com.rhymthwave.Request.DTO.Top10ArtistDTO;
import com.rhymthwave.Request.DTO.Top10PodcastDTO;
import com.rhymthwave.entity.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {


    private final ArtistDAO artistDAO;

    private final GenreDAO genreDAO;

    private final UserTypeDAO userTypeDAO;

    private  final AdvertismentDAO advertismentDAO;

    private  final  AccountDAO accountDAO;

    private final RecordDAO recordDAO;

    private final EpisodeDAO episodeDAO;

    private Set<String> uniqueVisitors = new HashSet<>();


    public List<Top10ArtistDTO> top10ArtistByListened() {

        return artistDAO.top10ArtistByListened();
    }

    public List<Top10PodcastDTO> top10PodcastByListened() {

        return accountDAO.getTopPodcast();
    }


    public Map<String, List<?>> top4Genre() {
        Map<String, Integer> genreSizeMap = new HashMap<>();

        var listTop4Genre = genreDAO.findAll();

        // Tính toán và thêm kích thước của mỗi thể loại vào Map
        for (Genre genre : listTop4Genre) {
            int size = genre.getSongGenres().size();
            genreSizeMap.put(genre.getNameGenre(), size);
        }

        // Sắp xếp Map theo thứ tự giảm dần theo kích thước
        List<Map.Entry<String, Integer>> sortedGenreList = genreSizeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(4)
                .toList();

        // Tạo danh sách tên và danh sách kích thước tương ứng
        List<String> nameList = sortedGenreList.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Integer> sortedTop4List = sortedGenreList.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        // Tạo một Map để chứa thông tin kết quả
        Map<String, List<?>> result = new HashMap<>();
        result.put("nameGenre", nameList);
        result.put("top4Genre", sortedTop4List);

        return result;
    }

    public int countSubscriptionCurrent(){
        return advertismentDAO.countSubscriptionCurrent() + userTypeDAO.countSubscriptionCurrent();
    }

    public Object top1Country(){
        return accountDAO.getTop1Country();
    }

    public int countAllAccount(){
        return accountDAO.countAll();
    }

    public int countAccountCreatedCurren(){ return  accountDAO.countAccountCreatedToday();}
    
    public int getCountAccountIsNotVerified(){ return  accountDAO.countAccountIsNotVerified();}

    public int incrementCounts(HttpSession session) {
        String sessionId = session.getId();
        uniqueVisitors.add(sessionId);
        return uniqueVisitors.size();
    }

    public Map<String,List<NumberCreateRecordAndEpisodeByDate>> numberCreateRecordsAndEpisodesByDay(String date){
        Map<String,List<NumberCreateRecordAndEpisodeByDate>> map = new HashMap<>();
        List<NumberCreateRecordAndEpisodeByDate> recordByDates = recordDAO.countCreateRecordsByDay(date);
        List<NumberCreateRecordAndEpisodeByDate> episodeByDatesByDates = episodeDAO.countCreateEpisodeByDay(date);
        map.put("record",recordByDates);
        map.put("episode",episodeByDatesByDates);
        return map;
    }

    

}
