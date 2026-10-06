package com.baraigad.ourwork.repository;
import com.baraigad.ourwork.entity.OurWork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OurWorkRepository extends JpaRepository<OurWork, Long> {
    List<OurWork> findByDelFlgFalseOrderByCreatedDateDesc();
}
