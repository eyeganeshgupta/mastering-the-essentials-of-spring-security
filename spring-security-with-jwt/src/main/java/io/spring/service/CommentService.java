package io.spring.service;

import io.spring.dto.CommentDTO;
import io.spring.response.ApiResponse;

import java.util.List;

public interface CommentService {

    ApiResponse<CommentDTO> createComment(Long postId, CommentDTO commentDTO);

    ApiResponse<List<CommentDTO>> getAllCommentsByPostId(Long postId);

    ApiResponse<CommentDTO> getCommentById(Long commentId);

    ApiResponse<CommentDTO> updateComment(Long commentId, CommentDTO commentDTO);

    ApiResponse<String> deleteComment(Long commentId);
}
