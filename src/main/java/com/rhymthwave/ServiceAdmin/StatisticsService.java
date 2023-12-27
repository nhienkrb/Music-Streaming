package com.rhymthwave.ServiceAdmin;

import com.rhymthwave.DAO.*;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Subscription;
import com.rhymthwave.entity.TypeEnum.EROLE;
import com.rhymthwave.entity.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final SongDAO songDAO;
    private final RecordDAO recordDAO;
    private final PlaylistDAO playlistDAO;
    private final AlbumDAO albumDAO;
    private final PodcastDAO podcastDAO;
    private final EpisodeDAO episodeDAO;
    private final AccountDAO accountDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final UserTypeDAO userTypeDAO;
    private final AdvertismentDAO advertismentDAO;


    public Map<String, Integer> getStatisticsOverview() {
        Map<String, Integer> map = new HashMap<>();
        map.put("song", songDAO.countSong());
        map.put("record", recordDAO.countRecording());
        map.put("playlist", playlistDAO.countPlaylist());
        map.put("album", albumDAO.countAlbum());
        map.put("episode", episodeDAO.countEpisode());
        map.put("podcast", podcastDAO.countPodcast());
        return map;
    }

    public List<Map<Integer, Long>> getAllAccountByYear() {
        return accountDAO.countAccountsByYear();
    }

    public List<Integer> getAllAccountByRole() {
        List<Integer> list = new ArrayList<>();
        list.add(accountDAO.findAllAccountRole(EROLE.USER).size());
        list.add(accountDAO.findAllAccountRole(EROLE.ARTIST).size());
        list.add(accountDAO.findAllAccountRole(EROLE.PODCAST).size());
        list.add(accountDAO.findAllAccountRole(EROLE.STAFF).size());
        return list;
    }

    public Map<String, List<Double>> totalSumPriceSubscriptionUsingByCategory() {
        Map<String, List<Double>> map = new HashMap<>();
        List<Double> listMonth = new ArrayList<>();
        List<Double> totalSumPriceAccountByMonth = new ArrayList<>();
        List<Double> totalSumPriceAdsByMonth = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            List<Subscription> subscriptionList = subscriptionDAO.getByMonth(i);
            if (!subscriptionList.isEmpty()) {
                double sum = 0.0;
                double sum2 = 0.0;
                double total = 0.0;

                for (var subscription : subscriptionList) {
                    if (subscription.getSubscriptionType().equalsIgnoreCase("BASIC") ||
                            subscription.getSubscriptionType().equalsIgnoreCase("MASTER")) {
                        continue;
                    } else {
                        if (!subscription.getUserTypes().isEmpty()) {
                            sum = sum + (subscription.getUserTypes().size() * subscription.getPrice());
                        } else if (!subscription.getAdvertisement().isEmpty()) {
                            sum2 = sum2 + (subscription.getAdvertisement().size() * subscription.getPrice());
                        }
                    }
                    total = sum + sum2;
                }
                totalSumPriceAccountByMonth.add(sum);
                totalSumPriceAdsByMonth.add(sum2);
                listMonth.add(total);
            } else {
                totalSumPriceAccountByMonth.add(0.0);
                totalSumPriceAdsByMonth.add(0.0);
                listMonth.add(0.0);
            }
        }

        map.put("sumPrice", listMonth);
        map.put("accountPrice", totalSumPriceAccountByMonth);
        map.put("adsPrice", totalSumPriceAdsByMonth);
        return map;
    }

    public Map<String, List<Integer>> totalSumPriceSubscriptionAllYear() {
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> totalSumList = new ArrayList<>();
        var allSubscriptionYear = subscriptionDAO.getAllSubscriptionYear();
        for (var year : allSubscriptionYear) {
            int sumPriceAccount = 0;
            int sumPriceAds = 0;
            int totalSum = 0;
            var listIdSubscriptionFromUserType = userTypeDAO.findIdSubscriptionByYearFromUserType(year);
            var listIdSubscriptionFromAds = advertismentDAO.findIdSubscriptionByYearFromAds(year);
            for (int idSubscriptionFromUserType : listIdSubscriptionFromUserType) {
                Subscription subscription = subscriptionDAO.findById(idSubscriptionFromUserType).orElseThrow();
                sumPriceAccount += subscription.getPrice();
            }
            for (int idSubscriptionFromAds : listIdSubscriptionFromAds) {
                Subscription SubscriptionFromAds = subscriptionDAO.findById(idSubscriptionFromAds).orElseThrow();
                sumPriceAds += SubscriptionFromAds.getPrice();
            }
            totalSum = sumPriceAccount + sumPriceAds;
            totalSumList.add(totalSum);
        }
        map.put("year", allSubscriptionYear);
        map.put("sumPrice", totalSumList);
        return map;
    }

    public List<Double> upgradePackageRate() {
        List<Double> list = new ArrayList<>();
        List<Account> accountList = accountDAO.findAll();
        List<UserType> totalAccountsPremium = userTypeDAO.findUserTypeByNameType("PREMIUM");
        double totalAccountBasis = accountList.size() - totalAccountsPremium.size();
        double rateBasic = 0.0;
        double ratePremium = 0.0;
        double avg;
        if (totalAccountBasis > 0) {
            avg = totalAccountBasis - totalAccountsPremium.size();
            rateBasic = avg / totalAccountBasis * 100;
        } else {
            avg = totalAccountsPremium.size() - totalAccountBasis;
            rateBasic = avg / totalAccountsPremium.size() * 100;
        }
        ratePremium = 100 - rateBasic;
        list.add(rateBasic);
        list.add(ratePremium);
        return list;
    }
}
