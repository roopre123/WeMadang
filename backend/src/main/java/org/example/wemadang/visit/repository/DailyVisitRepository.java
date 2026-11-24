package org.example.wemadang.visit.repository;

import org.example.wemadang.visit.domain.DailyVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyVisitRepository extends JpaRepository<DailyVisit, Long> {
}
