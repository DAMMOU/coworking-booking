package com.coworkinglab.coworkingbooking.application.port.in;

import com.coworkinglab.coworkingbooking.application.command.BookRoomCommand;
import com.coworkinglab.coworkingbooking.domain.model.Reservation;

public interface BookRoomUseCase {

    Reservation book(BookRoomCommand command);
}