package com.etchapedia.home;

import com.etchapedia.comment.Comments;
import com.etchapedia.comment.Reply;
import com.etchapedia.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Likes {
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="comment_idx")
	private Comments comment;
	
	@ManyToOne
	@JoinColumn(name="reply_idx")
	private Reply reply;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="like")
	@SequenceGenerator(name="like", sequenceName="seq_like_idx", allocationSize=1)
	private Integer likeIdx;
}
