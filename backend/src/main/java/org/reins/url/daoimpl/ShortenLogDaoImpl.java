package org.reins.url.daoimpl;

import org.reins.url.dao.ShortenLogDao;
import org.reins.url.entity.ShortenLog;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.ShortenLogRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
@Service
public class ShortenLogDaoImpl implements ShortenLogDao {
  @Autowired
  private ShortenLogRepository shortenLogRepository;
  @Autowired
  private ShortenerRepository shortenerRepository;

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
  public void addShortenLog(long creatorId, List<String> shortUrls, List<String> longUrls) {
    ShortenLog shortenLog = null;
    String shortUrl = "";
    for (int i = 0; i < shortUrls.size(); ++i) {
      if (!shortUrls.get(i).equals(shortUrl)) {
        shortUrl = shortUrls.get(i);
        shortenLog = new ShortenLog();
        shortenLog.setShortUrl(shortUrl);
        shortenLog.setCreatorId(creatorId);
        shortenLog.setCreateTime(new Date());
        shortenLog.setVisitCount(0);
        shortenLogRepository.save(shortenLog);
      }
      Shortener shortener = new Shortener();
      assert shortenLog != null;
      shortener.setShortenId(shortenLog.getId());
      shortener.setLongUrl(longUrls.get(i));
      shortenerRepository.insert(shortener);
    }
  }

  @Override
  public ShortenLog findById(long id) {
    Optional<ShortenLog> shortenLogOptional = shortenLogRepository.findById(id);
    if (!shortenLogOptional.isPresent()) return null;
    ShortenLog shortenLog = shortenLogOptional.get();
    shortenLog.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(shortenLog.getId())));
    return shortenLog;
  }

  @Override
  public List<ShortenLog> findByCreatorId(long creatorId) {
    List<ShortenLog> list = shortenLogRepository.findByCreatorId(creatorId);
    for (ShortenLog value : list)
      value.setShortener(reorderShortenerList(shortenerRepository.findByShortenId(value.getId())));
    return list;
  }

  @Override
  public Optional<ShortenLog> findByShortUrl(String shortUrl) {
    return shortenLogRepository.findByShortUrl(shortUrl);
  }

  @Override
  public List<ShortenLog> findAll() {
    return shortenLogRepository.findAll();
  }

  @Override
  public long count() {
    return shortenLogRepository.count();
  }

  @Override
  public long visitSum() {
    return shortenLogRepository.visitSum();
  }
}
