package com.library.controller;

import com.library.dto.IssueRequest;
import com.library.model.IssueRecord;
import com.library.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/issue")
    public ResponseEntity<IssueRecord> issueBook(@Valid @RequestBody IssueRequest request) {
        IssueRecord issued = issueService.issueBook(request.getBookId(), request.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(issued);
    }

    @PutMapping("/return/{issueId}")
    public ResponseEntity<IssueRecord> returnBook(@PathVariable Long issueId) {
        IssueRecord returned = issueService.returnBook(issueId);
        return ResponseEntity.ok(returned);
    }

    @GetMapping
    public ResponseEntity<List<IssueRecord>> getAllIssues() {
        return ResponseEntity.ok(issueService.getAllIssues());
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueRecord> getIssueById(@PathVariable Long issueId) {
        return ResponseEntity.ok(issueService.getIssueById(issueId));
    }
}
