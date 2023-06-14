package chamapool.domain.loans.repositories;

import chamapool.domain.loans.LoanApplication;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {}
