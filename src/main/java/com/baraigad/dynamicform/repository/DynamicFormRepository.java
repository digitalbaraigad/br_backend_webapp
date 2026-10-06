package com.baraigad.dynamicform.repository;
import com.baraigad.dynamicform.entity.DynamicForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface DynamicFormRepository extends JpaRepository<DynamicForm, Long> {
    Optional<DynamicForm> findByPublicId(String publicId);
    List<DynamicForm> findAllByOrderByCreatedAtDesc();
}
