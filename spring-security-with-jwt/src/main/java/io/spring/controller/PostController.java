package io.spring.controller;

import io.spring.dto.CustomPaginationAPIResponse;
import io.spring.dto.PostDTO;
import io.spring.response.ApiResponse;
import io.spring.service.PostService;
import io.spring.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostDTO>> createPost(@Valid @RequestBody PostDTO postDTO) {
        ApiResponse<PostDTO> response = postService.createPost(postDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts() {
        ApiResponse<List<PostDTO>> response = postService.getAllPosts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable Long id) {
        ApiResponse<PostDTO> response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        ApiResponse<PostDTO> response = postService.updatePost(id, postDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        ApiResponse<Void> response = postService.deletePost(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<CustomPaginationAPIResponse>> getPaginatedPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO + "", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE + "", required = false) int pageSize) {
        ApiResponse<CustomPaginationAPIResponse> response = postService.getPaginatedPosts(pageNo, pageSize);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/paginated-and-sorted")
    public ResponseEntity<ApiResponse<CustomPaginationAPIResponse>> getPaginatedAndSortedPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO + "", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE + "", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        ApiResponse<CustomPaginationAPIResponse> response = postService.getPaginatedAndSortedPosts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
