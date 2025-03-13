package com.harbourspace.cs308.service;

import com.harbourspace.cs308.model.TrackedRepository;
import com.harbourspace.cs308.model.RepositoryActivity;
import com.harbourspace.cs308.model.RepositoryActivityType;
import com.harbourspace.cs308.repository.RepositoryActivityRepository;
import com.harbourspace.cs308.repository.TrackedRepositoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private TrackedRepositoryRepository trackedRepositoryRepository;
    
    @Autowired
    private RepositoryActivityRepository activityRepository;
    
    public Map<String, Object> getAggregatedStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<TrackedRepository> repositories = trackedRepositoryRepository.findAll();
        int totalRepositories = repositories.size();
        stats.put("totalRepositories", totalRepositories);
        
        Map<String, Integer> activityCount = new HashMap<>();
        for (RepositoryActivityType type : RepositoryActivityType.values()) {
            if (type == RepositoryActivityType.UNKNOWN) {
                continue;
            }

            Long count = activityRepository.countByType(type);
            activityCount.put(type.name(), count.intValue());
        }
        stats.put("activityCount", activityCount);
        
        return stats;
    }
}
