package org.example.wemadang.visit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_visits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyVisit {

    @Id
    private LocalDate visitDate;

    @Column(nullable = false)
    private long count;
}
