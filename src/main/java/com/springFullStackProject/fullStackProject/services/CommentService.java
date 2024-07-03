package com.springFullStackProject.fullStackProject.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springFullStackProject.fullStackProject.entities.Comment;
import com.springFullStackProject.fullStackProject.entities.Post;
import com.springFullStackProject.fullStackProject.entities.User;
import com.springFullStackProject.fullStackProject.repos.CommentRepository;
import com.springFullStackProject.fullStackProject.requests.CommentCreateRequest;
import com.springFullStackProject.fullStackProject.requests.CommentUpdateRequest;

@Service
public class CommentService {

	private CommentRepository commentRepository;
	private UserServices userService;
	private PostService postService;
	
	public CommentService(CommentRepository commentRepository, 
			UserServices userService, PostService postService) {
		this.commentRepository = commentRepository;
		this.userService = userService;
		this.postService = postService;
	}

	public List<Comment> getAllComments(Optional<Long> userId, Optional<Long> postId) {
		if(userId.isPresent() && postId.isPresent()) {
			return commentRepository.findByUserIdAndPostId(userId.get(), postId.get());
		}
		if (userId.isPresent()) {
			return commentRepository.findByUserId(userId.get());
		}
		else if(postId.isPresent()) {
			return commentRepository.findByPostId(postId.get());
		}
		else return commentRepository.findAll();
	}

	public Comment getOneComment(Long commentId) {
		return commentRepository.findById(commentId).orElse(null);
	}

	public Comment createOneComment(CommentCreateRequest commentRequest) {
		User user = userService.getOneUser(commentRequest.getUserId());
		Post post = postService.getOnePost(commentRequest.getPostId());
		if (user != null && post != null) {
			Comment commentToSave = new Comment();
			commentToSave.setId(commentRequest.getId());
			commentToSave.setText(commentRequest.getText());
			commentToSave.setUser(user);
			commentToSave.setPost(post);
			return commentRepository.save(commentToSave);
		}
		else return null;
	}

	public Comment updateOneComment(Long commentId, CommentUpdateRequest updatedComment) {
		Optional <Comment> comment = commentRepository.findById(commentId);
		if (comment.isPresent()) {
			Comment toUpdate = comment.get();
			toUpdate.setText(updatedComment.getText());
			commentRepository.save(toUpdate);
			return toUpdate;
		}
		return null;
	}

	public void deleteOneComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}
	
	
}
