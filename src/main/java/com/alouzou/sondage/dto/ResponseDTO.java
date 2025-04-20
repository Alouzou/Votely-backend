package com.alouzou.sondage.dto;

import com.alouzou.sondage.entities.Response;
import com.alouzou.sondage.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long choiceId;

    public static ResponseDTO fromEntity(Response response) {
        if (response == null) {
            return null;
        }
        return ResponseDTO.builder()
                .userId(response.getUser().getId())
                .choiceId(response.getChoice().getId())
                .build();
    }

    public static Response toEntity(ResponseDTO responseDto, User user) {
        if (responseDto == null) {
            return null;
        }
        Response response = new Response();
        response.setUser(user);

        return response;
    }
}
