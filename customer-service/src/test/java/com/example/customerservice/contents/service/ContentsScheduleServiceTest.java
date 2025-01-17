package com.example.customerservice.contents.service;

import com.example.customerservice.contents.client.BookApiClient;
import com.example.customerservice.contents.entity.Contents;
import com.example.customerservice.contents.repository.ContentsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ContentsScheduleServiceTest {
    @Mock
    private BookApiClient bookApiClient;

    @Mock
    private ContentsRepository contentsRepository;

    @InjectMocks
    private ContentsSchedulerService contentsSchedulerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("API로부터 총 페이지 수를 계산합니다.")
    void checkTotalPages_ShouldReturnCorrectPageCount() throws JsonProcessingException {
        // Given
        int maxResults = 50;
        String response = "{" +
                "\"totalResults\": 120" +
                "}";
        when(bookApiClient.fetchBooksByPage(1, maxResults)).thenReturn(response);

        // When
        int totalPages = contentsSchedulerService.checkTotalPages(maxResults);

        // Then
        assertThat(totalPages).isEqualTo(3);
    }

    @Test
    @DisplayName("중복되지 않은 책만 데이터베이스에 저장하며, 중복된 데이터는 무시합니다.")
    void saveContentsToDatabase_ShouldIgnoreDuplicateBooks() {
        // Given
        List<String> existingTitlesAndWriters = List.of("Title1|Author1", "Title2|Author2");
        when(contentsRepository.findAllTitlesAndWriters()).thenReturn(existingTitlesAndWriters);

        List<Contents> contentsList = IntStream.range(1, 6)
                .mapToObj(i -> Contents.of("Book", "Title" + i, "Author" + i, "Description" + i, "https://example.com/image" + i + ".jpg"))
                .collect(Collectors.toList());

        // When
        contentsSchedulerService.saveContentsToDatabase(contentsList);

        // Then
        verify(contentsRepository, times(1)).saveAll(argThat(newContents -> {
            List<String> savedTitlesAndWriters = StreamSupport.stream(newContents.spliterator(), false)
                    .map(content -> content.getTitle() + "|" + content.getWriter())
                    .toList();

            List<String> expectedNewTitlesAndWriters = List.of("Title3|Author3", "Title4|Author4", "Title5|Author5");
            return savedTitlesAndWriters.containsAll(expectedNewTitlesAndWriters) &&
                    savedTitlesAndWriters.size() == expectedNewTitlesAndWriters.size();
        }));

        List<String> allTitlesAndWriters = Stream.concat(
                existingTitlesAndWriters.stream(),
                List.of("Title3|Author3", "Title4|Author4", "Title5|Author5").stream()
        ).toList();

        assertThat(allTitlesAndWriters).hasSize(5);
    }

}
