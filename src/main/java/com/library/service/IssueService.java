package com.library.service;

import com.library.model.IssueRecord;

import java.util.List;

public interface IssueService {

    IssueRecord issueBook(Long bookId, Long memberId);

    IssueRecord returnBook(Long issueId);

    List<IssueRecord> getAllIssues();

    IssueRecord getIssueById(Long issueId);
}
