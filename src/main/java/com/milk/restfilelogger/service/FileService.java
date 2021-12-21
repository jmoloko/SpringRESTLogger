package com.milk.restfilelogger.service;

import com.milk.restfilelogger.dto.FileDTO;
import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.exception.UserAlreadyExistException;
import com.milk.restfilelogger.exception.UserNotFoundException;

import java.util.List;

/**
 * @author Jack Milk
 */
public interface FileService {
    List<FileDTO> getAll();
    FileDTO getFileById(Long id) throws FileNotFoundException;
    List<FileDTO> getFilesByUserId(Long id);
    FileDTO renameFile(FileEntity file, Long id) throws FileAlreadyExistException, FileNotFoundException;
    FileDTO save(FileEntity file) throws FileAlreadyExistException;
    Long delete(Long id);
}
