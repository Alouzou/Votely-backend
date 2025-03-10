package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Response;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    private Long userId;
    private Long questionId;
    private Long choiceId;

    public static ResponseDTO fromEntity(Response response) {
        if (response == null) {
            return null;
        }
        return ResponseDTO.builder()
                .userId(response.getUser().getId())
                .questionId(response.getQuestion().getId())
                .choiceId(response.getChoice().getId())
                .build();
    }

    public static Response toEntity(ResponseDTO dto) {
        if (dto == null) {
            return null;
        }
        Response response = new Response();
        return response;
    }
}
