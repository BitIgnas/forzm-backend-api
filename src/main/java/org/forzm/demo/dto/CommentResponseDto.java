package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String content;
    private Instant dateReplied;
    private String userUsername;
    private String userDateCreated;
    private String userProfileImageUrl;
}
