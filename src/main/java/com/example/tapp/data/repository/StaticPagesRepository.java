package com.example.tapp.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.data.entities.StaticPages;

public interface StaticPagesRepository extends JpaRepository<StaticPages, Long>{

	Optional<StaticPages> findByType(PageType pageType);

}
