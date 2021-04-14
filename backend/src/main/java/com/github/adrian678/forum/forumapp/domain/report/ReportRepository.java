package com.github.adrian678.forum.forumapp.domain.report;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, ReportId> {
}
