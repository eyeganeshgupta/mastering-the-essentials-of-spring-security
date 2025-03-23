package io.spring.service;

import io.spring.dto.CustomPaginationAPIResponse;
import io.spring.dto.PostDTO;
import io.spring.response.ApiResponse;

import java.util.List;

public interface PostService {

    ApiResponse<PostDTO> createPost(PostDTO postDTO);

    ApiResponse<List<PostDTO>> getAllPosts();

    ApiResponse<PostDTO> getPostById(Long id);

    ApiResponse<PostDTO> updatePost(Long id, PostDTO postDTO);

    ApiResponse<Void> deletePost(Long id);

    ApiResponse<CustomPaginationAPIResponse> getPaginatedPosts(int pageNo, int pageSize);

    ApiResponse<CustomPaginationAPIResponse> getPaginatedAndSortedPosts(int pageNo, int pageSize, String sortBy, String sortDir);

}
