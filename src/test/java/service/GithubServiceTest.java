
package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import com.example.githubsearcher.Repository.GithubRepositoryRepository;
import com.example.githubsearcher.dto.GithubSearchRequest;
import com.example.githubsearcher.model.GithubRepository;
import com.example.githubsearcher.service.GithubService;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

    @Mock
    private GithubRepositoryRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubService githubService;

    @Test
    void shouldReturnRepositories() {

        List<GithubRepository> expected = Collections.emptyList();

        when(repository.findFilteredRepositories(
                "Java", 1000,
                Sort.by(Sort.Direction.DESC, "forks")
        )).thenReturn(expected);

        List<GithubRepository> actual =
                githubService.getRepositories("Java", 1000, "forks");

        assertEquals(expected, actual);

        verify(repository).findFilteredRepositories(
                "Java", 1000,
                Sort.by(Sort.Direction.DESC, "forks")
        );
    }

    @Test
    void shouldSortByStarsByDefault() {

        List<GithubRepository> expected = Collections.emptyList();

        when(repository.findFilteredRepositories(
                null, null,
                Sort.by(Sort.Direction.DESC, "stars")
        )).thenReturn(expected);

        List<GithubRepository> actual =
                githubService.getRepositories(null, null, null);

        assertEquals(expected, actual);

        verify(repository).findFilteredRepositories(
                null, null,
                Sort.by(Sort.Direction.DESC, "stars")
        );
    }

    @Test
    void shouldSortByUpdatedDate() {

        List<GithubRepository> expected = Collections.emptyList();

        when(repository.findFilteredRepositories(
                "Java", null,
                Sort.by(Sort.Direction.DESC, "lastUpdated")
        )).thenReturn(expected);

        List<GithubRepository> actual =
                githubService.getRepositories("Java", null, "updated");

        assertEquals(expected, actual);

        verify(repository).findFilteredRepositories(
                "Java", null,
                Sort.by(Sort.Direction.DESC, "lastUpdated")
        );
    }

    @Test
    void shouldFetchAndSaveRepositories() throws Exception {

        String json = """
                {
                  "items": [
                    {
                      "id": 123,
                      "name": "spring-boot",
                      "description": "Spring Boot Framework",
                      "owner": {"login": "spring-projects"},
                      "language": "Java",
                      "stargazers_count": 50000,
                      "forks_count": 30000,
                      "updated_at": "2026-11-18T10:00:00Z"
                    }
                  ]
                }
                """;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(json);

        when(restTemplate.getForObject(
                anyString(),
                org.mockito.ArgumentMatchers.eq(JsonNode.class)
        )).thenReturn(response);

        when(repository.findById(123L))
                .thenReturn(Optional.empty());

        GithubRepository savedRepository = new GithubRepository();
        savedRepository.setId(123L);
        savedRepository.setName("spring-boot");

        when(repository.saveAll(any()))
                .thenReturn(List.of(savedRepository));

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        List<GithubRepository> result =
                githubService.searchAndSave(request);

        assertEquals(1, result.size());
        assertEquals("spring-boot", result.get(0).getName());

        verify(restTemplate).getForObject(
                anyString(),
                org.mockito.ArgumentMatchers.eq(JsonNode.class)
        );

        verify(repository).saveAll(any());
    }
    @Test
    void shouldThrowExceptionForInvalidGitHubResponse() {

        GithubSearchRequest request = new GithubSearchRequest();
        request.setQuery("spring boot");
        request.setLanguage("Java");
        request.setSort("stars");

        when(restTemplate.getForObject(
                anyString(),
                org.mockito.ArgumentMatchers.eq(JsonNode.class)
        )).thenReturn(null);

        assertThrows(
                IllegalStateException.class,
                () -> githubService.searchAndSave(request)
        );
    }
}
