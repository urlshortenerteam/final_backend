package org.reins.url.serviceimpl;

import org.reins.url.dao.Edit_logDao;
import org.reins.url.service.Edit_logService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Edit_logServiceImpl implements Edit_logService {
  @Autowired
  private Edit_logDao edit_logDao;

  @Override
  public void addEdit_log(long editor_id, String shortener_id) {
    edit_logDao.addEdit_log(editor_id, shortener_id);
  }
}
