package io.spring.controller;

import io.spring.dto.CommentDTO;
import io.spring.response.ApiResponse;
import io.spring.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentDTO>> createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        ApiResponse<CommentDTO> response = commentService.createComment(postId, commentDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> getAllCommentsByPostId(@PathVariable Long postId) {
        ApiResponse<List<CommentDTO>> response = commentService.getAllCommentsByPostId(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        ApiResponse<CommentDTO> response = commentService.getCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        ApiResponse<CommentDTO> response = commentService.updateComment(commentId, commentDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        ApiResponse<String> response = commentService.deleteComment(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
