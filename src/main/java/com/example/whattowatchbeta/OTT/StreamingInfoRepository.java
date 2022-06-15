package com.example.whattowatchbeta.OTT;

import com.example.whattowatchbeta.OTT.StreamingModels.StreamingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamingInfoRepository extends JpaRepository<StreamingInfo,Long> {
}
