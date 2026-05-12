package com.library.repository;

import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Member CRUD operations.
 *
 * Team members can add custom finders as the application grows
 * (e.g., search by name, paginated list, etc.).
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /** Look up a member by their unique email address */
    Optional<Member> findByEmail(String email);

    /** Check whether an email is already registered (useful for validation) */
    boolean existsByEmail(String email);
}
