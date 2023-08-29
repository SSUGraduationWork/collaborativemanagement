package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Members;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface MembersRepository extends CrudRepository<Members, Long> {
    ArrayList<Members> findAll();
}
