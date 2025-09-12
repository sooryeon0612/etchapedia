package com.etchapedia.comment;

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
public class Reply {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reply")
	@SequenceGenerator(name="reply", sequenceName="seq_reply_idx", allocationSize=1)
	private Integer replyIdx;
	
	@ManyToOne
	@JoinColumn(name="user_idx")
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="comment_idx")
	private Comments comment;
	
	private String reply;
	
}
