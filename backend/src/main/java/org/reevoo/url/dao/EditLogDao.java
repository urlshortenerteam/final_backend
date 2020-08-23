package org.reevoo.url.dao;

public interface EditLogDao {
    void addEditLog(long editorId, String shortenerId);
}
