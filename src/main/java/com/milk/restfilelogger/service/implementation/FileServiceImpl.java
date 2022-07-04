package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.repository.FileRepository;
import com.milk.restfilelogger.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack Milk
 */

@RequiredArgsConstructor
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public List<FileEntity> getAll() {
        log.info("Fetching all files");
        return fileRepository.findAll();
    }

    @Override
    public FileEntity getFileById(Long id) throws FileNotFoundException {
        log.info("Fetching file by id: {}", id);
        return fileRepository.findById(id).orElseThrow(() -> {
            log.error("File with id: {} not found", id);
            return new FileNotFoundException("File NOT Found");
        });
    }

    public List<FileEntity> getFilesByUserId(Long id) {
        log.info("Fetching all files by user id: {}", id);
        return fileRepository.findAllByUserId(id);
    }

    @Override
    public FileEntity renameFile(FileEntity file) throws FileAlreadyExistException, FileNotFoundException {
        log.info("Update/rename file: {}", file.getName());
        FileEntity uFile = fileRepository.findById(file.getId()).orElseThrow(() -> {
            log.error("File not found");
            return new FileNotFoundException("File NOT Found");
        });

        log.info("Check new file name for uniqueness");
        if (fileRepository.findByName(file.getName()).isPresent() && file.getPath().equals(uFile.getPath())){
            log.error("File with name: {} already exist", file.getName());
            throw new FileAlreadyExistException("File already exist!");
        }

        return fileRepository.save(file);
    }

    @Override
    public FileEntity save(FileEntity file) throws FileAlreadyExistException {
        log.info("Saving new file: {}", file.getName());
        if (fileRepository.findByName(file.getName()).isPresent()){
            log.error("File with name: {} already exist", file.getName());
            throw new FileAlreadyExistException("File already exist!");
        }
        return fileRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete file file by id: {}", id);
        fileRepository.deleteById(id);
    }

}
