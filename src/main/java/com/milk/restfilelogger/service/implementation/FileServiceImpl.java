package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.repository.FileRepository;
import com.milk.restfilelogger.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack Milk
 */
@Service
public class FileServiceImpl implements FileService {


    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FileEntity> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public FileEntity getFileById(Long id) throws FileNotFoundException {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File NOT Found"));
    }

    public List<FileEntity> getFilesByUserId(Long id) {
        return fileRepository.findAllByUserId(id);
    }

    @Override
    public FileEntity renameFile(FileEntity file) throws FileAlreadyExistException, FileNotFoundException {
        FileEntity uFile = fileRepository.findById(file.getId()).orElseThrow(() -> new FileNotFoundException("File NOT Found"));
        if (fileRepository.findByName(file.getName()).isPresent() && file.getPath().equals(uFile.getPath())){
            throw new FileAlreadyExistException("File already exist!");
        }

        return fileRepository.save(file);
    }

    @Override
    public FileEntity save(FileEntity file) throws FileAlreadyExistException {
        if (fileRepository.findByName(file.getName()).isPresent()){
            throw new FileAlreadyExistException("File already exist!");
        }
        return fileRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        fileRepository.deleteById(id);
    }

}
