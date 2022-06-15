package com.example.whattowatchbeta.IMDB;

import com.example.whattowatchbeta.IMDB.Model.JobDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails,Long> {


    Optional<JobDetails> getByJobId(Long jobId);
}
