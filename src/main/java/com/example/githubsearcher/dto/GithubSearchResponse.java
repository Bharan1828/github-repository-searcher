package com.example.githubsearcher.dto;

import java.util.List;
import com.example.githubsearcher.model.GithubRepository;

public class GithubSearchResponse {

    private String message;
    private List<GithubRepository> repositories;

    public GithubSearchResponse() {
    }

    public GithubSearchResponse(String message,
            List<GithubRepository> repositories) {
        this.message = message;
        this.repositories = repositories;
    }

    public String getMessage() {
        return message;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }
}