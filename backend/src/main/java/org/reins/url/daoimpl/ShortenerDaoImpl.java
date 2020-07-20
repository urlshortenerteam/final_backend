package org.reins.url.daoimpl;

import org.reins.url.dao.ShortenerDao;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Transactional
@Repository
@Service
public class ShortenerDaoImpl implements ShortenerDao {
  @Autowired
  private ShortenerRepository shortenerRepository;

  private List<Shortener> reorderShortenerList(List<Shortener> shortenerList) {
    for (int i = 1; i < shortenerList.size(); i++)
      if (shortenerList.get(i).getLong_url().equals("BANNED")) {
        Collections.swap(shortenerList, i, 0);
        break;
      }
    return shortenerList;
  }

  @Override
  public void addShortener(long shorten_id, String short_url, String long_url) {
    Shortener shortener = new Shortener();
    shortener.setShorten_id(shorten_id);
    shortener.setShort_url(short_url);
    shortener.setLong_url(long_url);
    shortenerRepository.insert(shortener);
  }

  @Override
  public void changeLong_url(Shortener shortener) {
    shortenerRepository.save(shortener);
  }

  @Override
  public void deleteShortener(String id) {
    shortenerRepository.deleteById(id);
  }

  @Override
  public List<Shortener> findByShort_url(String short_url) {
    List<Shortener> shortenerList = shortenerRepository.findByShort_url(short_url);
    return reorderShortenerList(shortenerList);
  }
}
