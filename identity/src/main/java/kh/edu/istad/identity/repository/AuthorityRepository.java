package kh.edu.istad.identity.repository;


import kh.edu.istad.identity.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
