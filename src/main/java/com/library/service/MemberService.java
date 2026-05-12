package com.library.service;

import com.library.model.IssueRecord;
import com.library.model.Member;

import java.util.List;

public interface MemberService {

    Member registerMember(Member member);

    Member getMemberById(Long memberId);

    List<Member> getAllMembers();

    List<IssueRecord> getBooksIssuedToMember(Long memberId);
}

