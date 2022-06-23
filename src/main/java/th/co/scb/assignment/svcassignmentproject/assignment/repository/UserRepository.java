package th.co.scb.assignment.svcassignmentproject.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.scb.assignment.svcassignmentproject.assignment.data.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
