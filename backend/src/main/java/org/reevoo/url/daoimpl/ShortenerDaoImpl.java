package org.reevoo.url.daoimpl;

import org.reevoo.url.dao.ShortenerDao;
import org.reevoo.url.entity.EditLog;
import org.reevoo.url.entity.Shortener;
import org.reevoo.url.repository.EditLogRepository;
import org.reevoo.url.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
@Service
public class ShortenerDaoImpl implements ShortenerDao {
    @Autowired
    private EditLogRepository editLogRepository;
    @Autowired
    private ShortenerRepository shortenerRepository;

    @Override
    public void addShortener(long editorId, long shortenId, String longUrl) {
        Shortener shortener = new Shortener();
        shortener.setShortenId(shortenId);
        shortener.setLongUrl(longUrl);
        shortenerRepository.insert(shortener);
        EditLog editLog = new EditLog();
        editLog.setEditorId(editorId);
        editLog.setEditTime(new Date());
        editLog.setShortenerId(shortener.getId());
        editLogRepository.save(editLog);
    }

    @Override
    public void changeShortener(Shortener shortener) {
        shortenerRepository.save(shortener);
    }
}
