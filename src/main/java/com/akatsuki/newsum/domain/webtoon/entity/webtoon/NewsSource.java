package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news_source")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsSource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;
    
    @Column(nullable = false, length = 255)
    private String headline;
    
    @Column(nullable = false, length = 1000)
    private String url;
    
    @Builder
    public NewsSource(String headline, String url) {
        this.headline = headline;
        this.url = url;
    }
}
