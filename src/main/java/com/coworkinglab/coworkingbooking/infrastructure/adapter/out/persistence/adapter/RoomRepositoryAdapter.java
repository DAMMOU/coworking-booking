package com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.adapter;

import com.coworkinglab.coworkingbooking.application.port.out.RoomRepository;
import com.coworkinglab.coworkingbooking.domain.model.Room;
import com.coworkinglab.coworkingbooking.domain.model.RoomId;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import com.coworkinglab.coworkingbooking.infrastructure.adapter.out.persistence.repository.JpaRoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomRepositoryAdapter implements RoomRepository {

    private final JpaRoomRepository jpa;
    private final PersistenceMapper mapper;

    public RoomRepositoryAdapter(JpaRoomRepository jpa, PersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<Room> findById(RoomId id) {
        return jpa.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).toList();
    }
}