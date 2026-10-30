package com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web;

import com.coworkinglab.coworkingbooking.application.port.in.CheckRoomAvailabilityUseCase;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.dto.RoomResponse;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.in.web.mapper.WebMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final CheckRoomAvailabilityUseCase checkRoomAvailability;
    private final WebMapper mapper;

    public RoomController(CheckRoomAvailabilityUseCase checkRoomAvailability, WebMapper mapper) {
        this.checkRoomAvailability = checkRoomAvailability;
        this.mapper = mapper;
    }

    @GetMapping("/available")
    public List<RoomResponse> available(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return checkRoomAvailability.checkAvailability(from, to).stream()
                .map(mapper::toResponse)
                .toList();
    }
}