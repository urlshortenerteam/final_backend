package org.reins.url.serviceimpl;

import org.reins.url.dao.EditLogDao;
import org.reins.url.service.EditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditLogServiceImpl implements EditLogService {
    @Autowired
    private EditLogDao editLogDao;

    @Override
    public void addEditLog(long editorId, String shortenerId) {
        editLogDao.addEditLog(editorId, shortenerId);
    }
}
