package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // search by keywords,company name, description
    @Query("SELECT j FROM Job j WHERE j.title LIKE %:keyword% OR j.recruiter.companyName " +
            "LIKE %:keyword% OR j.keywords LIKE %:keyword% OR j.description LIKE %:keyword%")
    List<Job> searchJobs(@Param("keyword") String keyword);

    // get 3 latest jobs
    List<Job> findTop3ByOrderByPostedDateDesc();
}
