package org.reins.url.daoimpl;

import org.reins.url.dao.Edit_logDao;
import org.reins.url.entity.Edit_log;
import org.reins.url.repository.Edit_logRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
@Service
public class Edit_logDaoImpl implements Edit_logDao {
    @Autowired
    private Edit_logRepository edit_logRepository;

    @Override
    public void addEdit_log(long editor_id, String shortener_id) {
        Edit_log edit_log = new Edit_log();
        edit_log.setEditor_id(editor_id);
        edit_log.setEdit_time(new Date());
        edit_log.setShortener_id(shortener_id);
        edit_logRepository.save(edit_log);
    }
}
