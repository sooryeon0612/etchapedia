package com.etchapedia.comment;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsInfoDto {
	private Comments comment;
	private Integer likesCount;
	private boolean isLiked;
	private List<Reply> replies;

}
