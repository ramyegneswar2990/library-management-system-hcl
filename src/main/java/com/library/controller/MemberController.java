package com.library.controller;

import com.library.dto.MemberDTO;
import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> registerMember(@Valid @RequestBody MemberDTO dto) {
        Member member = Member.builder()
                .name(dto.name())
                .email(dto.email())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(member));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<IssueRecord>> getBooksIssuedToMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getBooksIssuedToMember(id));
    }
}

