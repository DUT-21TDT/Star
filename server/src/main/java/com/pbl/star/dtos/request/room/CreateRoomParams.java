package com.pbl.star.dtos.request.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomParams {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
