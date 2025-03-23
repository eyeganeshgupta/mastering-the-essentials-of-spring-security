package io.spring.service.impl;

import io.spring.dto.CommentDTO;
import io.spring.entity.Comment;
import io.spring.entity.Post;
import io.spring.exception.ResourceNotFoundException;
import io.spring.repository.CommentRepository;
import io.spring.repository.PostRepository;
import io.spring.response.ApiResponse;
import io.spring.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<CommentDTO> createComment(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return new ApiResponse<>(true, "Comment created successfully", modelMapper.map(savedComment, CommentDTO.class));
    }

    @Override
    public ApiResponse<List<CommentDTO>> getAllCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", String.valueOf(postId)));

        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentDTO> commentDTOS = comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Comments retrieved successfully", commentDTOS);
    }

    @Override
    public ApiResponse<CommentDTO> getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));
        return new ApiResponse<>(true, "Comment retrieved successfully", modelMapper.map(comment, CommentDTO.class));
    }

    @Override
    public ApiResponse<CommentDTO> updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));

        if (commentDTO.getName() != null) {
            comment.setName(commentDTO.getName());
        }
        if (commentDTO.getEmail() != null) {
            comment.setEmail(commentDTO.getEmail());
        }
        if (commentDTO.getText() != null) {
            comment.setText(commentDTO.getText());
        }

        Comment updatedComment = commentRepository.save(comment);

        return new ApiResponse<>(true, "Comment updated successfully", modelMapper.map(updatedComment, CommentDTO.class));
    }

    @Override
    public ApiResponse<String> deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", String.valueOf(commentId)));
        commentRepository.deleteById(commentId);
        return new ApiResponse<>(true, "Comment deleted successfully", null);
    }
}
