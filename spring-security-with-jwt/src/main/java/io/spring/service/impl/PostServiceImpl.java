package io.spring.service.impl;

import io.spring.dto.CustomPaginationAPIResponse;
import io.spring.dto.PostDTO;
import io.spring.entity.Post;
import io.spring.exception.ResourceNotFoundException;
import io.spring.repository.PostRepository;
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
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
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
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Posts retrieved successfully", postDTOs);
    }

    @Override
    public ApiResponse<PostDTO> getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return new ApiResponse<>(true, "Post retrieved successfully", postDTO);
    }

    @Override
    public ApiResponse<PostDTO> updatePost(Long id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setDescription(postDTO.getDescription());
        existingPost.setContent(postDTO.getContent());

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
                .map(post -> modelMapper.map(post, PostDTO.class))
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
                .map(post -> modelMapper.map(post, PostDTO.class))
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
