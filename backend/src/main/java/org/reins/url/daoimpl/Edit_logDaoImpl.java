package org.reins.url.daoimpl;

import org.reins.url.dao.Edit_logDao;
import org.reins.url.repository.Edit_logRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@Service
public class Edit_logDaoImpl implements Edit_logDao {
    @Autowired
    private Edit_logRepository edit_logRepository;
}
