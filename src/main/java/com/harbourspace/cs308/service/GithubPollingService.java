package com.harbourspace.cs308.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.harbourspace.cs308.model.RepositoryActivity;
import com.harbourspace.cs308.model.TrackedRepository;
import com.harbourspace.cs308.repository.RepositoryActivityRepository;
import com.harbourspace.cs308.repository.TrackedRepositoryRepository;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Phaser;

@Slf4j
@Service
public class GithubPollingService {
    @Autowired
    private GitHubClientService gitHubClientService;

    @Autowired
    private TrackedRepositoryRepository trackedRepoRepository;
    
    @Autowired
    private RepositoryActivityRepository activityRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TaskExecutor taskExecutor;

    // @Scheduled(fixedDelay = 50000)
    @Async
    @Scheduled(fixedRate = 5000)
    public void updateActivities() throws Exception {
        log.info("Updating activities");

        int pageNumber = 0;
        int pageSize = 500;
        Page<TrackedRepository> page;

        Phaser phaser = new Phaser(1);

        do {
            PageRequest pageable = PageRequest.of(pageNumber, pageSize);
            page = trackedRepoRepository.findByActiveTrue(pageable);
            List<TrackedRepository> chunk = page.getContent();
            log.info("Processing chunk of {} repositories", chunk.size());

            if (!chunk.isEmpty()) {
                phaser.register();

                taskExecutor.execute(() -> {
                    processChunk(chunk);
                    phaser.arriveAndDeregister(); 
                });
            }
            
            pageNumber++;
        } while (page.hasNext());

        phaser.arriveAndAwaitAdvance();
    }

    private void processChunk(List<TrackedRepository> chunk) {
        for (TrackedRepository repo : chunk) {
            log.info("Processing repository {}/{}", repo.getOwner(), repo.getName());
            List<RepositoryActivity> activities = gitHubClientService.fetchActivities(repo);
            log.info("Fetched {} new activities", activities.size());

            transactionTemplate.executeWithoutResult(status -> {
                activityRepository.saveAll(activities);
                repo.setLastTrackedTime(Instant.now());
                trackedRepoRepository.save(repo);
            });            
        }
    }
}
