package com.milk.restfilelogger.service;

import com.milk.restfilelogger.entity.FileEntity;
import com.milk.restfilelogger.exception.FileAlreadyExistException;
import com.milk.restfilelogger.exception.FileNotFoundException;
import com.milk.restfilelogger.repository.FileRepository;
import com.milk.restfilelogger.service.implementation.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Jack Milk
 */

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTests {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    public FileEntity getFile() {
        FileEntity file = new FileEntity();
        file.setId(1L);
        file.setName("test_file.txt");
        file.setPath("/path/to/file");
        return file;
    }

    public FileEntity getNewFile() {
        FileEntity file = new FileEntity();
        file.setId(1L);
        file.setName("new_test_file.txt");
        file.setPath("/path/to/file");
        return file;
    }

    public List<FileEntity> getFiles() {
        return Stream.of(
                new FileEntity("test_file.txt", "/path/to/file"),
                new FileEntity("new_file.txt", "/path/to/file"),
                new FileEntity("some_file.txt", "/path/to/file")
        ).collect(Collectors.toList());
    }

    @Test
    public void getAllFilesTest() {
        when(fileRepository.findAll()).thenReturn(getFiles());
        assertEquals(3, fileService.getAll().size());
        assertEquals("test_file.txt", fileService.getAll().get(0).getName());
        assertEquals("/path/to/file", fileService.getAll().get(0).getPath());

        verify(fileRepository, times(3)).findAll();
    }

    @Test
    public void getFileByIdTest() throws FileNotFoundException {
        when(fileRepository.findById(1L)).thenReturn(Optional.of(getFile()));
        assertEquals("test_file.txt", fileService.getFileById(1L).getName());
        assertEquals("/path/to/file", fileService.getFileById(1L).getPath());

        verify(fileRepository, times(2)).findById(1L);
    }

    @Test
    public void getFilesByUserIdTest() {
        when(fileRepository.findAllByUserId(1L)).thenReturn(getFiles());
        assertEquals(3, fileService.getFilesByUserId(1L).size());
        assertEquals("test_file.txt", fileService.getFilesByUserId(1L).get(0).getName());
        assertEquals("/path/to/file", fileService.getFilesByUserId(1L).get(0).getPath());

        verify(fileRepository, times(3)).findAllByUserId(1L);
    }

    @Test
    public void saveFileTest() throws FileAlreadyExistException {
        when(fileRepository.save(Mockito.any(FileEntity.class))).thenReturn(getFile());
        assertEquals(1L, fileService.save(getFile()).getId());
        assertEquals("test_file.txt", fileService.save(getFile()).getName());
        assertEquals("/path/to/file", fileService.save(getFile()).getPath());

        verify(fileRepository, times(3)).save(Mockito.any(FileEntity.class));
    }

    @Test
    public void renameFileTest() throws FileAlreadyExistException, FileNotFoundException {
        when(fileRepository.findById(1L)).thenReturn(Optional.of(getFile()));
        when(fileRepository.save(Mockito.any(FileEntity.class))).thenReturn(getNewFile());
        assertEquals("new_test_file.txt", fileService.renameFile(getNewFile()).getName());
        assertEquals(1L, fileService.renameFile(getNewFile()).getId());
        assertEquals("/path/to/file", fileService.renameFile(getNewFile()).getPath());

        verify(fileRepository, times(3)).save(Mockito.any(FileEntity.class));

    }

    @Test
    public void deleteFileTest() {
        doNothing().when(fileRepository).deleteById(1L);
        fileService.delete(1L);
        verify(fileRepository).deleteById(1L);
    }
}
