package com.smarterwith.catalogservice.repository;

import com.smarterwith.catalogservice.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, String>{
}
