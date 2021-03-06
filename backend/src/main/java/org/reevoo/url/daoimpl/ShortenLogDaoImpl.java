package org.reevoo.url.daoimpl;

import org.reevoo.url.dao.ShortenLogDao;
import org.reevoo.url.entity.ShortenLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.repository.ShortenLogRepository;
import org.reevoo.url.repository.ShortenerRepository;
import org.reevoo.url.xeger.Xeger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Transactional
@Repository
@Service
public class ShortenLogDaoImpl implements ShortenLogDao {
    @Autowired
    private ShortenLogRepository shortenLogRepository;
    @Autowired
    private ShortenerRepository shortenerRepository;

    private String long2short(String longUrl) {
        Date date = new Date();
        Xeger xeger = new Xeger("[A-Za-z0-9]{6}", new Random(date.getTime()));
        String hex = DigestUtils.md5DigestAsHex((xeger.generate() + longUrl).getBytes());
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            long hexLong = 0x3fffffff & Long.parseLong(hex.substring(i * 8, i * 8 + 8), 16);
            StringBuilder outChars = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                long index = 0x3d & hexLong;
                if (index < 26) outChars.append((char) ('A' + index));
                else if (index < 52) outChars.append((char) ('a' + index - 26));
                else outChars.append((char) ('0' + index - 52));
                hexLong >>= 5;
            }
            res.add(outChars.toString());
        }
        return res.get((int) (Math.random() * 4));
    }

    private List<Shortener> reorderShortenerList(List<Shortener> shortenerList) {
        List<Shortener> res = new ArrayList<>();
        Shortener shortener = null;
        long cnt = 0;
        for (Shortener value : shortenerList) {
            String longUrl = value.getLongUrl();
            if (longUrl.equals("BANNED")) {
                cnt++;
                if (shortener == null || value.getId().compareTo(shortener.getId()) > 0) shortener = value;
            }
            if (longUrl.equals("LIFT")) cnt--;
        }
        if (cnt > 0) res.add(shortener);
        for (Shortener value : shortenerList) {
            String longUrl = value.getLongUrl();
            if (!longUrl.equals("BANNED") && !longUrl.equals("LIFT")) res.add(value);
        }
        return res;
    }

    @Override
    public String addOneShortenLog(long creatorId, List<String> longUrls) {
        if (longUrls.isEmpty()) return "";
        ShortenLog shortenLog = new ShortenLog();
        shortenLog.setCreatorId(creatorId);
        shortenLog.setCreateTime(new Date());
        shortenLog.setVisitCount(0);
        while (true) {
            String shortUrl = long2short(longUrls.get((int) (Math.random() * longUrls.size())));
            if (shortenLogRepository.findByShortUrl(shortUrl) == null) {
                shortenLog.setShortUrl(shortUrl);
                shortenLogRepository.save(shortenLog);
                break;
            }
        }
        for (String longUrl : longUrls) {
            Shortener shortener = new Shortener();
            shortener.setShortenId(shortenLog.getId());
            shortener.setLongUrl(longUrl);
            shortenerRepository.insert(shortener);
        }
        return shortenLog.getShortUrl();
    }

    @Override
    public List<String> addShortenLog(long creatorId, List<String> longUrls) {
        List<String> shortUrls = new ArrayList<>();
        for (String longUrl : longUrls) {
            ShortenLog shortenLog = new ShortenLog();
            shortenLog.setCreatorId(creatorId);
            shortenLog.setCreateTime(new Date());
            shortenLog.setVisitCount(0);
            while (true) {
                String shortUrl = long2short(longUrl);
                if (shortenLogRepository.findByShortUrl(shortUrl) == null) {
                    shortenLog.setShortUrl(shortUrl);
                    shortenLogRepository.save(shortenLog);
                    break;
                }
            }
            shortUrls.add(shortenLog.getShortUrl());
            Shortener shortener = new Shortener();
            shortener.setShortenId(shortenLog.getId());
            shortener.setLongUrl(longUrl);
            shortenerRepository.insert(shortener);
        }
        return shortUrls;
    }

    @Override
    public long count() {
        return shortenLogRepository.count();
    }

    @Override
    public List<ShortenLog> findAll() {
        List<ShortenLog> shortenLogList = shortenLogRepository.findAll();
        for (ShortenLog shortenLog : shortenLogList)
            shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLogList;
    }

    @Override
    public List<ShortenLog> findByCreatorId(long creatorId) {
        List<ShortenLog> shortenLogList = shortenLogRepository.findByCreatorId(creatorId);
        for (ShortenLog shortenLog : shortenLogList)
            shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLogList;
    }

    @Override
    public Page<ShortenLog> findByCreatorIdPageable(long creatorId, Pageable pageable) {
        Page<ShortenLog> shortenLogList = shortenLogRepository.findByCreatorId(creatorId, pageable);
        for (ShortenLog shortenLog : shortenLogList)
            shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLogList;
    }

    @Override
    public ShortenLog findByShortUrl(String shortUrl) {
        ShortenLog shortenLog = shortenLogRepository.findByShortUrl(shortUrl);
        if (shortenLog == null) return null;
        shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLog;
    }

    @Override
    public Page<ShortenLog> findPage(Pageable pageable) {
        Page<ShortenLog> shortenLogList = shortenLogRepository.findAll(pageable);
        for (ShortenLog shortenLog : shortenLogList)
            shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLogList;
    }

    @Override
    public ShortenLog findTopOneOrderByVisitCount() {
        return shortenLogRepository.findTopByOrderByVisitCountDesc();
    }

    @Override
    public List<ShortenLog> findTopTenOrderByVisitCount() {
        List<ShortenLog> shortenLogList = shortenLogRepository.findTop10ByOrderByVisitCountDesc();
        for (ShortenLog shortenLog : shortenLogList)
            shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
        return shortenLogList;
    }

    @Override
    public List<Shortener> getUserShorteners(List<Long> shortenLogID) {
        return shortenerRepository.findByShortenIdIn(shortenLogID);
    }

    @Override
    public List<ShortenLog> getUserShortenLogs(long id) {
        return shortenLogRepository.findByCreatorId(id);
    }

    @Override
    public long visitSum() {
        Long res = shortenLogRepository.visitSum();
        if (res == null) return 0;
        return res;
    }
}
