package com.example.githubsearcher.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="github_repositories")
public class GithubRepository {
    @Id
	private Long id;
    private String name;
    
    @Column(length=2000)
    private String description;
    
    @Column(name="owner_login")
    private String owner;
    
    private String language;
    
	@Column(name="stars_count")
    private Integer stars;
    
    @Column(name="forks_count")
    private Integer forks;
    
    @Column(name="last_updated")
    private Instant lastUpdated;
    
    public Long getId() {
  		return id;
  	}

  	public void setId(Long id) {
  		this.id = id;
  	}

  	public String getName() {
  		return name;
  	}

  	public void setName(String name) {
  		this.name = name;
  	}

  	public String getDescription() {
  		return description;
  	}

  	public void setDescription(String description) {
  		this.description = description;
  	}

  	public String getOwner() {
  		return owner;
  	}

  	public void setOwner(String owner) {
  		this.owner = owner;
  	}

  	public String getLanguage() {
  		return language;
  	}

  	public void setLanguage(String language) {
  		this.language = language;
  	}

  	public Integer getStars() {
  		return stars;
  	}

  	public void setStars(Integer stars) {
  		this.stars = stars;
  	}

  	public Integer getForks() {
  		return forks;
  	}

  	public void setForks(Integer forks) {
  		this.forks = forks;
  	}

  	public Instant getLastUpdated() {
  		return lastUpdated;
  	}

  	public void setLastUpdated(Instant lastUpdated) {
  		this.lastUpdated = lastUpdated;
  	}

    
    public GithubRepository() {
    	
    }
}
