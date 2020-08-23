package org.reevoo.url.repository;

import org.reevoo.url.entity.EditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditLogRepository extends JpaRepository<EditLog, Long> {
}
