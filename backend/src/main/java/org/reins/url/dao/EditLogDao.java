package org.reins.url.dao;

public interface EditLogDao {
  void addEditLog(long editorId, String shortenerId);
}
