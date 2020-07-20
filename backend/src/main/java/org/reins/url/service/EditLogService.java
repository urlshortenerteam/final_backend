package org.reins.url.service;

public interface EditLogService {
  void addEditLog(long editorId, String shortenerId);
}
