package com.milk.restfilelogger.repository;

import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query("SELECT DISTINCT f FROM FileEntity as f LEFT JOIN EventEntity e on f.id = e.file.id WHERE e.user.id = ?1")
    List<FileEntity> findAllByUserId(Long id);
    FileEntity findByName(String filename);
}
