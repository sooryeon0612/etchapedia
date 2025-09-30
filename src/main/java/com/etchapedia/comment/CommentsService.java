package com.etchapedia.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
	@Autowired
	private CommentsRepository cmRepo;
	
	public Comments save(Comments comment) {
		return cmRepo.save(comment);
	}
}
