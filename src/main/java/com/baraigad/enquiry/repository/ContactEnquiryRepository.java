package com.baraigad.enquiry.repository;

import com.baraigad.enquiry.entity.ContactEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactEnquiryRepository extends JpaRepository<ContactEnquiry, Long> {
}
