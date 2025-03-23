package io.spring.service.impl;

import io.spring.dto.CommentDTO;
import io.spring.dto.CustomPaginationAPIResponse;
import io.spring.dto.PostDTO;
import io.spring.entity.Post;
import io.spring.entity.Comment;
import io.spring.exception.ResourceNotFoundException;
import io.spring.repository.PostRepository;
import io.spring.repository.CommentRepository;
import io.spring.response.ApiResponse;
import io.spring.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<PostDTO> createPost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        Post savedPost = postRepository.save(post);
        PostDTO savedPostDTO = modelMapper.map(savedPost, PostDTO.class);
        return new ApiResponse<>(true, "Post created successfully", savedPostDTO);
    }

    @Override
    public ApiResponse<List<PostDTO>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOs = posts.stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPost(post);
                    List<CommentDTO> commentDTOs = comments.stream()
                            .map(comment -> modelMapper.map(comment, CommentDTO.class))
                            .collect(Collectors.toList());

                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    postDTO.setComments(commentDTOs);
                    return postDTO;
                })
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Posts retrieved successfully", postDTOs);
    }

    @Override
    public ApiResponse<PostDTO> getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        List<Comment> comments = commentRepository.findByPost(post);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());

        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setComments(commentDTOs);

        return new ApiResponse<>(true, "Post retrieved successfully", postDTO);
    }

    @Override
    public ApiResponse<PostDTO> updatePost(Long id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        if (postDTO.getTitle() != null && !postDTO.getTitle().trim().isEmpty()) {
            existingPost.setTitle(postDTO.getTitle());
        }

        if (postDTO.getDescription() != null && !postDTO.getDescription().trim().isEmpty()) {
            existingPost.setDescription(postDTO.getDescription());
        }

        if (postDTO.getContent() != null && !postDTO.getContent().trim().isEmpty()) {
            existingPost.setContent(postDTO.getContent());
        }

        Post updatedPost = postRepository.save(existingPost);

        PostDTO updatedPostDTO = modelMapper.map(updatedPost, PostDTO.class);
        return new ApiResponse<>(true, "Post updated successfully", updatedPostDTO);
    }

    @Override
    public ApiResponse<Void> deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post", "id", id.toString());
        }
        postRepository.deleteById(id);
        return new ApiResponse<>(true, "Post deleted successfully", null);
    }

    @Override
    public ApiResponse<CustomPaginationAPIResponse> getPaginatedPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> page = postRepository.findAll(pageable);
        List<PostDTO> postDTOs = page.getContent().stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPost(post);
                    List<CommentDTO> commentDTOs = comments.stream()
                            .map(comment -> modelMapper.map(comment, CommentDTO.class))
                            .collect(Collectors.toList());
                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    postDTO.setComments(commentDTOs);
                    return postDTO;
                })
                .collect(Collectors.toList());

        CustomPaginationAPIResponse response = new CustomPaginationAPIResponse(
                postDTOs,
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return new ApiResponse<>(true, "Posts retrieved successfully", response);
    }

    @Override
    public ApiResponse<CustomPaginationAPIResponse> getPaginatedAndSortedPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> page = postRepository.findAll(pageable);
        List<PostDTO> postDTOs = page.getContent().stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPost(post);
                    List<CommentDTO> commentDTOs = comments.stream()
                            .map(comment -> modelMapper.map(comment, CommentDTO.class))
                            .collect(Collectors.toList());
                    PostDTO postDTO = modelMapper.map(post, PostDTO.class);
                    postDTO.setComments(commentDTOs);
                    return postDTO;
                })
                .collect(Collectors.toList());

        CustomPaginationAPIResponse response = new CustomPaginationAPIResponse(
                postDTOs,
                page.getNumber(),
                page.getSize(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );

        return new ApiResponse<>(true, "Posts retrieved successfully", response);
    }
}
