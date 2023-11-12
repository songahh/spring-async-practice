package com.example.async.controller;

import com.example.async.entity.BoardImg;
import com.example.async.service.BoardImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BoardImgController {

    private final BoardImgService is;

    @PostMapping(value = "/imgs/async", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<String> saveImgsAsync(@RequestParam(value="imgs")MultipartFile files) throws Exception{
        log.info("[ASYNC] controller {}", Thread.currentThread().getName());
        List<BoardImg> imgs = is.parseCsvFile(files);
        is.saveImgAsync(imgs);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/imgs/sync", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<String> saveImgs(@RequestParam(value="imgs")MultipartFile files) throws Exception{
        log.info("[SYNC] controller {}", Thread.currentThread().getName());
        List<BoardImg> imgs = is.parseCsvFile(files);
        is.saveImg(imgs);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/imgs/async", produces = "application/json")
    public CompletableFuture<ResponseEntity> findImgsAsync(){
        return is.findAllImgsAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/imgs/sync", produces = "application/json")
    public ResponseEntity findImgs(){
        return ResponseEntity.ok().body(is.findAllImgs());
    }
}
