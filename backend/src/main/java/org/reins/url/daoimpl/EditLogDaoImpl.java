package org.reins.url.daoimpl;

import org.reins.url.dao.EditLogDao;
import org.reins.url.entity.EditLog;
import org.reins.url.repository.EditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
@Service
public class EditLogDaoImpl implements EditLogDao {
    @Autowired
    private EditLogRepository editLogRepository;

    @Override
    public void addEditLog(long editorId, String shortenerId) {
        EditLog editLog = new EditLog();
        editLog.setEditorId(editorId);
        editLog.setEditTime(new Date());
        editLog.setShortenerId(shortenerId);
        editLogRepository.save(editLog);
    }
}
