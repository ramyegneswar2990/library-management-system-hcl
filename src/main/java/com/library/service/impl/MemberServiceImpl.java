package com.library.service.impl;

import com.library.exception.ResourceNotFoundException;
import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final IssueRecordRepository issueRecordRepository;

    public MemberServiceImpl(MemberRepository memberRepository, IssueRecordRepository issueRecordRepository) {
        this.memberRepository = memberRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    @Override
    public Member registerMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        return memberRepository.save(member);
    }

    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public List<IssueRecord> getBooksIssuedToMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found with id: " + memberId);
        }
        return issueRecordRepository.findByMember_MemberIdAndReturnDateIsNull(memberId);
    }
}

