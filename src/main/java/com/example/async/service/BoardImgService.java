package com.example.async.service;

import com.example.async.entity.BoardImg;
import com.example.async.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardImgService {
    private final BoardImgRepository ir;

//    public CompletableFuture<List<BoardImg>> saveImgSync(MultipartFile file) throws Exception{
//        long start = System.currentTimeMillis();
//        List<BoardImg> imgs = parseCsvFile(file);
//        log.info("saving list of imgs of size {}", imgs.size(), "" + Thread.currentThread().getName());
//        imgs = ir.saveAll(imgs);
//        long end = System.currentTimeMillis();
//        log.info("total time {} ms", end-start);
//        return CompletableFuture.completedFuture(imgs);
//    }

    @Async
    public CompletableFuture<List<BoardImg>> saveImgAsync(List<BoardImg> imgs) throws Exception{
        long start = System.currentTimeMillis();
        log.info("[ASYNC] saving list of imgs of size {}, {}", imgs.size(), Thread.currentThread().getName());
        imgs = ir.saveAll(imgs);
        long end = System.currentTimeMillis();
        log.info("total time {} ms", end-start);
        return CompletableFuture.completedFuture(imgs);
    }

    public List<BoardImg> saveImg(List<BoardImg> imgs) throws Exception{
        long start = System.currentTimeMillis();
        log.info("[SYNC] saving list of imgs of size {}, {}", imgs.size(), Thread.currentThread().getName());
        imgs = ir.saveAll(imgs);
        long end = System.currentTimeMillis();
        log.info("total time {} ms", end-start);
        return imgs;
    }


    public  List<BoardImg> findAllImgs(){
        log.info("get list of imgs by " + Thread.currentThread().getName());
        return ir.findAll();
    }

    @Async
    public  CompletableFuture<List<BoardImg>> findAllImgsAsync(){
        log.info("get list of imgs by " + Thread.currentThread().getName());
        List<BoardImg> imgs = ir.findAll();
        return CompletableFuture.completedFuture(imgs);
    }

    public List<BoardImg> parseCsvFile(final MultipartFile file) throws Exception{
        final List<BoardImg> imgs = new ArrayList<>();

        try{
            try(final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                String line;
                while((line=br.readLine())!=null){
                    final String[] data = line.split(",");
                    final BoardImg img = new BoardImg();
//                    img.setId(Integer.parseInt(data[0]));
                    img.setOriginalFileName(data[1]);
                    img.setImgUrl(data[2]);
                    imgs.add(img);
                }
                return imgs;
            }
        } catch(final IOException e){
            e.printStackTrace();
            log.error("Failed to parse CSV file {}", e.getMessage());
            throw new Exception("Failed to parse CSV file {}", e);
        }

    }
}
