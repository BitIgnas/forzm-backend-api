package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.Post;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @Lob
    @NotNull
    private String content;
    @NotNull
    private String postTitle;
    @NotNull
    private Long postId;
}
