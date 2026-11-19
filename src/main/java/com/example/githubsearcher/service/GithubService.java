
package com.example.githubsearcher.service;

import com.example.githubsearcher.model.GithubRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.githubsearcher.Repository.GithubRepositoryRepository;
import com.example.githubsearcher.dto.GithubSearchRequest;

import tools.jackson.databind.JsonNode;

@Service
public class GithubService {

    private final GithubRepositoryRepository repository;
    private final RestTemplate restTemplate;

    public GithubService(GithubRepositoryRepository repository,
                         RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    // Build GitHub API URL
    private String buildGitHubUrl(String query, String language, String sort) {

        StringBuilder url = new StringBuilder(
                "https://api.github.com/search/repositories?q="
        );

        url.append(URLEncoder.encode(query, StandardCharsets.UTF_8));

        if (language != null && !language.isBlank()) {
            url.append("+language:")
               .append(URLEncoder.encode(language, StandardCharsets.UTF_8));
        }

        if (sort != null && !sort.isBlank()) {
            url.append("&sort=").append(sort);
        }

        return url.toString();
    }

    // Fetch repositories from GitHub API
    private JsonNode fetchRepositories(String url) {

        JsonNode response = restTemplate.getForObject(
                url,
                JsonNode.class
        );

        return response;
    }

    // Save new repositories or update existing repositories
    private List<GithubRepository> saveRepositories(JsonNode response) {

        List<GithubRepository> repositories = new ArrayList<>();

        // Validate GitHub API response
        if (response == null ||
                !response.has("items") ||
                !response.get("items").isArray()) {

            throw new IllegalStateException(
                    "Invalid response received from GitHub API"
            );
        }

        JsonNode items = response.get("items");

        for (JsonNode item : items) {

            Long id = item.get("id").asLong();

            // Check whether repository already exists in PostgreSQL
            GithubRepository githubRepository =
                    repository.findById(id).orElse(new GithubRepository());

            // Set latest GitHub data
            githubRepository.setId(id);
            githubRepository.setName(item.get("name").asText());

            githubRepository.setDescription(
                    item.get("description").isNull()
                    ? null : item.get("description").asText()
            );

            githubRepository.setOwner(
                    item.get("owner").get("login").asText()
            );

            githubRepository.setLanguage(
                    item.get("language").isNull()
                    ? null : item.get("language").asText()
            );

            githubRepository.setStars(
                    item.get("stargazers_count").asInt()
            );

            githubRepository.setForks(
                    item.get("forks_count").asInt()
            );

            githubRepository.setLastUpdated(
                    Instant.parse(item.get("updated_at").asText())
            );

            repositories.add(githubRepository);
        }

        return repository.saveAll(repositories);
    }

    // Search GitHub and save repositories in PostgreSQL
    public List<GithubRepository> searchAndSave(GithubSearchRequest request) {

        String url = buildGitHubUrl(
                request.getQuery(),
                request.getLanguage(),
                request.getSort()
        );

        JsonNode response = fetchRepositories(url);

        return saveRepositories(response);
    }

    // Retrieve repositories from PostgreSQL
    public List<GithubRepository> getRepositories(
            String language, Integer minStars, String sort) {

        Sort sorting;

        // Sort by forks
        if ("forks".equalsIgnoreCase(sort)) {

            sorting = Sort.by(Sort.Direction.DESC, "forks");

        }
        // Sort by last updated date
        else if ("updated".equalsIgnoreCase(sort)) {

            sorting = Sort.by(Sort.Direction.DESC, "lastUpdated");

        }
        // Default sorting: stars
        else {

            sorting = Sort.by(Sort.Direction.DESC, "stars");
        }

        return repository.findFilteredRepositories(
                language,
                minStars,
                sorting
        );
    }
}
