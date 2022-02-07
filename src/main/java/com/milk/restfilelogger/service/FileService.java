package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface FileService {
    List<FileEntity> getAll();
    FileEntity getFileById(Long id) throws FileNotFoundException;
    List<FileEntity> getFilesByUserId(Long id);
    FileEntity renameFile(FileEntity file) throws FileAlreadyExistException, FileNotFoundException;
    FileEntity save(FileEntity file) throws FileAlreadyExistException;
    void delete(Long id);
}
