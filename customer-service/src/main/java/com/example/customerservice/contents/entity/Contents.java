package com.example.customerservice.contents.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="contents")
@NoArgsConstructor
public class Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @Column
    private String summary;

    @Column
    private String image;

    @Column
    private Long heartCount;

    @Builder
    public Contents(String category, String title, String writer, String summary, String image, Long heartCount) {
        this.category = category;
        this.title = title;
        this.writer = writer;
        this.summary = summary;
        this.image = image;
        this.heartCount = heartCount;
    }

    public static Contents of(String category, String title, String writer, String summary, String image) {
        return Contents.builder()
                .category(category)
                .title(title)
                .writer(writer)
                .summary(summary)
                .image(image)
                .heartCount(0L) // 기본값 설정
                .build();
    }

}
