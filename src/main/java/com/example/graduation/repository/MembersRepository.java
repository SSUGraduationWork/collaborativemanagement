package com.example.graduation.repository;

import com.example.graduation.entity.Members;
import com.example.graduation.entity.Minutes;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public interface MembersRepository extends CrudRepository<Members, Long> {
    ArrayList<Members> findAll();
}
