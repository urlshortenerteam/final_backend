package org.reevoo.url.serviceimpl;

import org.reevoo.url.dao.EditLogDao;
import org.reevoo.url.service.EditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EditLogServiceImpl implements EditLogService {
    @Autowired
    private EditLogDao editLogDao;

    @Override
    @Async
    public void addEditLog(long editorId, String shortenerId) {
        editLogDao.addEditLog(editorId, shortenerId);
    }
}
