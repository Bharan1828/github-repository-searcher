
package com.example.githubsearcher.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.example.githubsearcher.model.GithubRepository;
import com.example.githubsearcher.dto.GithubRepositoriesResponse;
import com.example.githubsearcher.dto.GithubSearchRequest;
import com.example.githubsearcher.dto.GithubSearchResponse;
import com.example.githubsearcher.service.GithubService;

@RestController
@RequestMapping("/api/github")
@Tag(
    name = "GitHub Repository Searcher",
    description = "APIs to search GitHub repositories and retrieve saved repositories"
)
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    // Search GitHub repositories and save them in PostgreSQL
    @Operation(
        summary = "Search GitHub repositories",
        description = "Searches GitHub repositories using query, language and sort, then saves the results in PostgreSQL"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Repositories fetched and saved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "message": "Repositories fetched and saved successfully",
                      "repositories": [
                        {
                          "id": 123456,
                          "name": "spring-boot-example",
                          "description": "Spring Boot example project",
                          "owner": "user123",
                          "language": "Java",
                          "stars": 450,
                          "forks": 120,
                          "lastUpdated": "2024-01-01T12:00:00Z"
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request"
        ),
        @ApiResponse(
            responseCode = "502",
            description = "Invalid response received from GitHub API"
        )
    })
    @PostMapping("/search")
    public GithubSearchResponse searchRepositories(
            @Valid @RequestBody GithubSearchRequest request) {

        List<GithubRepository> repositories =
                githubService.searchAndSave(request);

        return new GithubSearchResponse(
                "Repositories fetched and saved successfully",
                repositories
        );
    }

    // Retrieve saved repositories from PostgreSQL
    @Operation(
        summary = "Get saved repositories",
        description = "Retrieves repositories from PostgreSQL with optional language, minimum stars and sorting filters"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Repositories retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "repositories": [
                        {
                          "id": 123456,
                          "name": "spring-boot-example",
                          "description": "Spring Boot example project",
                          "owner": "user123",
                          "language": "Java",
                          "stars": 450,
                          "forks": 120,
                          "lastUpdated": "2024-01-01T12:00:00Z"
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/repositories")
    public GithubRepositoriesResponse getRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(defaultValue = "stars") String sort) {

        List<GithubRepository> repositories =
                githubService.getRepositories(language, minStars, sort);

        return new GithubRepositoriesResponse(repositories);
    }
}
