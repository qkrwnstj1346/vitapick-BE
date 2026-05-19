package com.vita.vitapickBack.products.prd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrdRepository extends JpaRepository<Prd, Long> {
}