package org.reins.url.daoimpl;
import org.reins.url.dao.UrlDao;
import org.reins.url.entity.Shorten_log;
import org.reins.url.entity.Shortener;
import org.reins.url.entity.Users;
import org.reins.url.repository.Shorten_logRepository;
import org.reins.url.repository.ShortenerRepository;
import org.reins.url.repository.UserRepository;
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
public class UrlDaoImpl implements UrlDao {
    @Autowired
    private Shorten_logRepository shorten_logRepository;
    @Autowired
    private ShortenerRepository shortenerRepository;
    @Autowired
    private UserRepository usersRepository;
    @Override
    public void addShortenLog(long creator_id,List<String> shortUrls,List<String> longUrls) {
        Shorten_log shorten_log=shorten_logRepository.save(new Shorten_log(creator_id,new Date()));
        long shorten_id=shorten_log.getId();
        for (int i=0;i<shortUrls.size();++i) shortenerRepository.insert(new Shortener(shorten_id,shortUrls.get(i),longUrls.get(i)));
    }
    @Override
    public void changeUsersVisit_count(long id) {
        Optional<Users> usersOptional=usersRepository.findById(id);
        if (usersOptional.isPresent()) {
            Users users=usersOptional.get();
            usersRepository.save(new Users(id,users.getName(),users.getPassword(),users.getRole(),users.getVisit_count()+1));
        }
    }
    @Override
    public List<Shortener> findShortenerByShort_url(String short_url) {
        return shortenerRepository.findByShort_url(short_url);
    }
    @Override
    public Shorten_log findShorten_logById(long id) {
        Optional<Shorten_log> shorten_log=shorten_logRepository.findById(id);
        return shorten_log.orElse(null);
    }
}
