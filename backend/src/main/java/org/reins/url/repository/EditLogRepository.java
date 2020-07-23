package org.reins.url.repository;

import org.reins.url.entity.EditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditLogRepository extends JpaRepository<EditLog, Long> {
}
