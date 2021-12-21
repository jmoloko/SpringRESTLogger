package com.milk.restfilelogger.service.implementation;

import com.milk.restfilelogger.dto.FileDTO;
import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.repository.FileRepository;
import com.milk.restfilelogger.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Override
    public List<FileDTO> getAll() {
        return fileRepository.findAll().stream().map(FileDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public FileDTO getFileById(Long id) throws FileNotFoundException {
        return FileDTO.toDto(fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File NOT Found")));
    }

    public List<FileDTO> getFilesByUserId(Long id) {
        return fileRepository.findAllByUserId(id).stream().map(FileDTO::toDto).collect(Collectors.toList());
    }

    @Override
    public FileDTO renameFile(FileEntity file, Long id) throws FileAlreadyExistException, FileNotFoundException {
        FileEntity uFile = fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File NOT Found"));
        if (fileRepository.findByName(file.getName()) != null ){
            throw new FileAlreadyExistException("File already exist!");
        }
        uFile.setName(file.getName());
        return FileDTO.toDto(fileRepository.save(uFile));
    }

    @Override
    public FileDTO save(FileEntity file) throws FileAlreadyExistException {
        if (fileRepository.findByName(file.getName()) != null ){
            throw new FileAlreadyExistException("File already exist!");
        }
        return FileDTO.toDto(fileRepository.save(file));
    }

    @Override
    public Long delete(Long id) {
        fileRepository.deleteById(id);
        return id;
    }
}
