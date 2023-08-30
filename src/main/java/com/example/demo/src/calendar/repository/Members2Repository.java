package com.example.demo.src.calendar.repository;

import com.example.demo.src.calendar.entity.Members2;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface Members2Repository extends CrudRepository<Members2, Long> {
    ArrayList<Members2> findAll();
}
