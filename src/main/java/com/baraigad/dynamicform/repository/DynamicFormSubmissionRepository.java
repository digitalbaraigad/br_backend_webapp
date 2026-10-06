package com.baraigad.dynamicform.repository;
import com.baraigad.dynamicform.entity.DynamicFormSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DynamicFormSubmissionRepository extends JpaRepository<DynamicFormSubmission, Long> {
    List<DynamicFormSubmission> findByFormIdOrderBySubmittedAtDesc(Long formId);
    List<DynamicFormSubmission> findAllByOrderBySubmittedAtDesc();
    long countByFormId(Long formId);
}
