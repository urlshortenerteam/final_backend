package org.reins.url.daoimpl;
import org.reins.url.dao.Shorten_logDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Transactional
@Repository
@Service
public class Shorten_logDaoImpl implements Shorten_logDao {
    @Autowired
    Shorten_logRepository shorten_logRepository;
    @Autowired
    ShortenerRepository shortenerRepository;
    @Override
    public void addShorten_log(long creator_id, List<String> shortUrls, List<String> longUrls) {
        Shorten_log shorten_log=new Shorten_log();
        shorten_log.setCreator_id(creator_id);
        shorten_log.setCreate_time(new Date());
        shorten_logRepository.save(shorten_log);
        long shorten_id=shorten_log.getId();
        for (int i=0;i<shortUrls.size();++i) {
            Shortener shortener=new Shortener();
            shortener.setShorten_id(shorten_id);
            shortener.setShort_url(shortUrls.get(i));
            shortener.setLong_url(longUrls.get(i));
            shortenerRepository.insert(shortener);
        }
    }
    @Override
    public List<Shorten_log> findAll() {
        List<Shorten_log> list=shorten_logRepository.findAll();
        for (Shorten_log s:list) s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        return list;
    }
    @Override
    public Shorten_log findById(long id) {
        Optional<Shorten_log> shorten_logOptional=shorten_logRepository.findById(id);
        if (!shorten_logOptional.isPresent()) return null;
        Shorten_log shorten_log=shorten_logOptional.get();
        shorten_log.setShortener(shortenerRepository.findByShorten_id(shorten_log.getId()));
        return shorten_log;
    }
    @Override
    public List<Shorten_log> findByCreator_id(long creator_id) {
        List<Shorten_log> list=shorten_logRepository.findByCreator_id(creator_id);
        for (Shorten_log s:list) s.setShortener(shortenerRepository.findByShorten_id(s.getId()));
        return list;
    }
}
