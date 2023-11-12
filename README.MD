# SYNC & ASYNC Test
**CompletableFuture을 사용한 동기 & 비동기 테스트**  
1000개의 더미데이터가 포함된 MultipartFile을 POST & GET  


## 환경
* Apple M2, 24GB, Thread 수: 8개
* JDK: 1.8
* Spring Boot, H2 database, JPA 이용
* ThreadPoolTasxExecutor
  * CorePoolSize: 2
  * MaxPoolSize: 8
* Jmeter를 사용한 성능 테스트
  * Thread Group
    * Numbers of Threads (users): 50
    * Loop cnt: 1

## 결과
### SYNC
* SAVE: avg 3199 ms
* GET : avg 3132 ms
<img width="1536" alt="snyc summary report" src="https://github.com/songahh/spring-async-practice/assets/77879519/82e2749a-4317-4ac4-b62d-3843e42eb7d2">  



### ASYNC
* SAVE: avg 993 ms
* GET : avg 3637 ms
<img width="1536" alt="async summary report" src="https://github.com/songahh/spring-async-practice/assets/77879519/aa9c8919-f28e-4a58-924e-acd9f00767f2">  


### 왜 이렇게 나왔다고 생각하세요?
* SAVE의 Async의 경우 실제로 다 저장이 되지 않아도 바로 콜백함수를 리턴함
``` JAVA
@PostMapping(value = "/imgs/async", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
public ResponseEntity<String> saveImgsAsync(@RequestParam(value="imgs")MultipartFile files) throws Exception{
   log.info("[ASYNC] controller {}", Thread.currentThread().getName());
   List<BoardImg> imgs = is.parseCsvFile(files);
   is.saveImgAsync(imgs);
   return ResponseEntity.status(HttpStatus.CREATED).build();
}
```
* GET의 Async의 경우 thenApply()를 사용해서 결과가 보이도록 작성함. Service를 처리하는 Thread와 Controller를 사용하는 Thread가 다르기 때문에 시간이 더 발생했을 것이라고 생각함
``` JAVA
@GetMapping(value = "/imgs/async", produces = "application/json")
public CompletableFuture<ResponseEntity> findImgsAsync(){
  return is.findAllImgsAsync().thenApply(ResponseEntity::ok);
}
```
