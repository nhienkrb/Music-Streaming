package com.rhymthwave.Request.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePlaylistDTO {

    private String playlistName;
    private String playlistDescription;

}
