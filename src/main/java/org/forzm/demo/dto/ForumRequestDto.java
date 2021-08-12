package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.ForumGameType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumRequestDto {
    private String name;
    @Enumerated(EnumType.STRING)
    private ForumGameType forumGameType;
    @Lob
    private String description;
}
