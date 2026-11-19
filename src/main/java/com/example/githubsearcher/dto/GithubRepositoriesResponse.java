package com.example.githubsearcher.dto;

import java.util.List;
import com.example.githubsearcher.model.GithubRepository;

public class GithubRepositoriesResponse {

    private List<GithubRepository> repositories;

    public GithubRepositoriesResponse() {
    }

    public GithubRepositoriesResponse(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }
}