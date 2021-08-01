package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.model.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDto {
    @NotNull
    private String name;
    @Lob
    @NotNull
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ForumGameType forumGameType;
    @NotNull
    private String imageUrl;
}
