
package com.example.githubsearcher.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.githubsearcher.model.GithubRepository;

public interface GithubRepositoryRepository
        extends JpaRepository<GithubRepository, Long> {

	@Query("""
	        SELECT g FROM GithubRepository g
	        WHERE LOWER(g.language) = LOWER(COALESCE(:language, g.language))
	        AND (:minStars IS NULL OR g.stars >= :minStars)
	        """)
    List<GithubRepository> findFilteredRepositories(
            @Param("language") String language,
            @Param("minStars") Integer minStars,
            Sort sort
    );
}
