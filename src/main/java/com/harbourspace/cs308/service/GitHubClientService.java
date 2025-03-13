package com.harbourspace.cs308.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.harbourspace.cs308.model.RepositoryActivity;
import com.harbourspace.cs308.model.RepositoryActivityType;
import com.harbourspace.cs308.model.TrackedRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GitHubClientService {
    @Autowired
    private WebClient gitHubWebClient;

    public List<RepositoryActivity> fetchActivities(TrackedRepository repo) throws RuntimeException {
        Instant lastTrackedTime = repo.getLastTrackedTime() != null
                ? repo.getLastTrackedTime()
                : Instant.MIN;

        List<RepositoryActivity> activities = new ArrayList<>();
        int page = 1;
        final int perPage = 100;
        boolean continueFetching = true;

        try {
            while (continueFetching) {
                final int currentPage = page;

                log.info("Fetching page {} for repository {}/{}", currentPage, repo.getOwner(), repo.getName());
                log.info("Request: /repos/{}/{}/events?page={}&per_page={}",
                        repo.getOwner(), repo.getName(), currentPage, perPage);

                // Fetch a page of events                
                List<Map<String, Object>> events = gitHubWebClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/repos/{owner}/{repo}/events")
                                .queryParam("page", currentPage)
                                .queryParam("per_page", perPage)
                                .build(repo.getOwner(), repo.getName()))
                        .exchangeToMono(response -> {
                            if (response.statusCode().isError()) {
                                log.error("GitHub API returned error status: {}", response.statusCode());
                                return response.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Error response body: {}", errorBody);
                                        return response.createException().flatMap(Mono::error);
                                    });
                            }
                            return response.bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
                        })
                        .block();

                if (events == null || events.isEmpty()) {
                    break;
                }

                for (Map<String, Object> event : events) {
                    RepositoryActivity activity = convertToRepositoryActivity(event);

                    if (activity == null) {
                        continue;
                    }

                    if (activity.getTimestamp().isAfter(lastTrackedTime)) {
                        activity.setRepository(repo);
                        activities.add(activity);
                    } else {
                        continueFetching = false;
                        break;
                    }
                }

                if (events.size() < perPage) {
                    break;
                }
                page++;
            }
            return activities;
        } catch (Exception ex) {
            String message = String.format("Failed to fetch activities for repository %s/%s", repo.getOwner(),
                    repo.getName());
            throw new RuntimeException(message, ex);
        }
    }

    private RepositoryActivity convertToRepositoryActivity(Map<String, Object> event) {
        String eventType = (String) event.get("type");
        RepositoryActivityType type = mapEventTypeToActivityType(eventType);

        if (type == RepositoryActivityType.UNKNOWN) {
            return null;
        }

        String createdAt = (String) event.get("created_at");
        LocalDateTime activityTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME);
        String description = generateDescriptionForEvent(event, eventType);

        RepositoryActivity activity = new RepositoryActivity();
        activity.setType(type);
        activity.setTimestamp(activityTime.toInstant(ZoneOffset.UTC));
        activity.setDescription(description);
        return activity;
    }

    private RepositoryActivityType mapEventTypeToActivityType(String eventType) {
        switch (eventType) {
            case "PushEvent":
                return RepositoryActivityType.COMMIT;
            case "IssuesEvent":
                return RepositoryActivityType.ISSUE;
            case "PullRequestEvent":
                return RepositoryActivityType.PULL_REQUEST;
            case "ReleaseEvent":
                return RepositoryActivityType.RELEASE;
            default:
                return RepositoryActivityType.UNKNOWN;
        }
    }

    @SuppressWarnings("unchecked")
    private String generateDescriptionForEvent(Map<String, Object> eventMap, String eventType) {
        Map<String, Object> payload = (Map<String, Object>) eventMap.get("payload");
        if (payload == null) {
            return "No description available";
        }
        switch (eventType) {
            case "PushEvent":
                List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
                int commitCount = commits != null ? commits.size() : 0;
                return "Pushed " + commitCount + " commit(s)";
            case "IssuesEvent":
                String issueAction = (String) payload.get("action");
                Map<String, Object> issue = (Map<String, Object>) payload.get("issue");
                String issueTitle = issue != null ? (String) issue.get("title") : "Unknown Issue";
                return "Issue " + issueAction + ": " + issueTitle;
            case "PullRequestEvent":
                String prAction = (String) payload.get("action");
                Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
                String prTitle = pullRequest != null ? (String) pullRequest.get("title") : "Unknown PR";
                return "Pull Request " + prAction + ": " + prTitle;
            case "ReleaseEvent":
                String releaseAction = (String) payload.get("action");
                Map<String, Object> release = (Map<String, Object>) payload.get("release");
                String tagName = release != null ? (String) release.get("tag_name") : "Unknown Release";
                return "Release " + releaseAction + ": " + tagName;
            default:
                return "Event type: " + eventType;
        }
    }
}
