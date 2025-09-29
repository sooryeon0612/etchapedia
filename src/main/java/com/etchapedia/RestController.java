package com.etchapedia;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.etchapedia.book.Book;
import com.etchapedia.book.BookService;
import com.etchapedia.book.GptRecommendationsService;
import com.etchapedia.comment.Likes;
import com.etchapedia.comment.LikesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/ajax")
public class RestController {
	@Autowired
	private GptRecommendationsService gSvc;
	@Autowired
	private BookService bSvc;
	@Autowired
	private LikesService lSvc;
	
	@PostMapping("load_recommend_books")
	public List<Book> loadRecommendBook(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		LocalDate lastUpdate = gSvc.getLastUpdateDate(userIdx);
    	if(!lastUpdate.equals(LocalDate.now())) {
    		List<Book> recommedList = gSvc.getGptRecommendBooks(userIdx);
    		gSvc.logRecommendation(recommedList, userIdx);
    	}
    	return gSvc.getRecommendedBooks(1);
	}
	
	@PostMapping("load_new_books")
	public List<Book> loadNewBooks(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		String search = root.path("search").asText();
		bSvc.saveNewBooksFromKeyword(search);
		return bSvc.getSearchBooks(search);
	}
	
	@PostMapping("load_reply_likes")
	public Map<Integer, List<Likes>> loadReplyLikes(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer commentIdx = root.path("commentIdx").asInt();
    	return lSvc.getReplyLikesByCommentIdx(commentIdx);
	}
	
	@PostMapping("insert_comment_like")
	public void insertCommentLike(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		Integer commentIdx = root.path("commentIdx").asInt();
		lSvc.insertCommentLike(userIdx, commentIdx);
	}
	
	@PostMapping("delete_comment_like")
	public void deleteCommentLike(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		Integer commentIdx = root.path("commentIdx").asInt();
		lSvc.deleteCommentLike(userIdx, commentIdx);
	}
	
	@PostMapping("insert_reply_like")
	public void insertReplyLike(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		Integer replyIdx = root.path("replyIdx").asInt();
		lSvc.insertReplyLike(userIdx, replyIdx);
	}
	
	@PostMapping("delete_reply_like")
	public void deleteReplyLike(@RequestBody String dataBody) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(dataBody);
		Integer userIdx = root.path("userIdx").asInt();
		Integer replyIdx = root.path("replyIdx").asInt();
		lSvc.deleteReplyLike(userIdx, replyIdx);
	}
}
