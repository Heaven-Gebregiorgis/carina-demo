package com.qaprosoft.carina.demo;


import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import com.qaprosoft.apitools.validation.JsonCompareKeywords;
import com.qaprosoft.carina.core.foundation.IAbstractTest;
import com.qaprosoft.carina.core.foundation.api.APIMethodPoller;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import com.qaprosoft.carina.core.foundation.utils.tag.Priority;
import com.qaprosoft.carina.core.foundation.utils.tag.TestPriority;
import com.qaprosoft.carina.demo.api.DeleteCommentMethod;
import com.qaprosoft.carina.demo.api.DeletePostMethod;
import com.qaprosoft.carina.demo.api.GetPostMethod;
import com.qaprosoft.carina.demo.api.PostCommentMethod;
import com.qaprosoft.carina.demo.api.PostUserMethod;


public class APIJsonPlaceHolderTest implements IAbstractTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	   @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testGetPosts() {
	        GetPostMethod getPostMethod = new GetPostMethod();
	        getPostMethod.callAPIExpectSuccess();
	        getPostMethod.getResponse();

	    }
	   
	   
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    @TestPriority(Priority.P1)
	    public void testDeletePosts() {
	        DeletePostMethod deletePostMethod = new DeletePostMethod();
	        deletePostMethod.setProperties("api/posts/post.properties");
	        deletePostMethod.callAPIExpectSuccess();
	        deletePostMethod.validateResponse();
	    }
	    
	    
	    
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testGetPostsSchema() {
	    	GetPostMethod getPostMethod = new GetPostMethod();
	    	getPostMethod.callAPIExpectSuccess();
	    	getPostMethod.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
	    	getPostMethod.validateResponseAgainstSchema("api/posts/_get/rs.schema");
	    }
	    
	    
	    
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testCreateUserWithSomeFields() throws Exception {
	        PostUserMethod api = new PostUserMethod();
	        api.setProperties("api/users/user.properties");
	        api.getProperties().remove("name");
	        api.getProperties().remove("username");
	        api.getProperties().remove("email");
	        api.callAPIExpectSuccess();
	        api.validateResponse();
	    }
	    
	   

	    
	    @Test
	    public void testCheckJSONSchema()
	    {
	    	PostUserMethod api = new PostUserMethod();
	        api.expectResponseStatus(HttpResponseStatusType.CREATED_201);
	        api.callAPI();
	        api.validateResponseAgainstSchema("api/users/_post/rs.schema");
	    }
	    
	    
	    
	    
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testCreateComment() throws Exception {
	        LOGGER.info("test");
	       // setCases("4555,54545");
	        PostCommentMethod api = new PostCommentMethod();
	        api.setProperties("api/comments/comment.properties");
	        api.callAPIExpectSuccess();
	        api.validateResponse();

	    }
	    
	    
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testCreateCommentWithMissingFields() throws Exception {

	        PostCommentMethod api = new PostCommentMethod();
	        api.setProperties("api/comments/comment.properties");
	        api.getProperties().remove("name");
	        api.getProperties().remove("email");
	        api.callAPIExpectSuccess();
	        api.validateResponse();
	        

	    }
	    
	    
	    
	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    @TestPriority(Priority.P2)
	    public void testDeleteComment() {
	        DeleteCommentMethod deleteCommentMethod = new DeleteCommentMethod();
	        deleteCommentMethod.setProperties("api/comments/comment.properties");
	        deleteCommentMethod.callAPIExpectSuccess();
	        deleteCommentMethod.validateResponse();
	    }
	    
	    

	    @Test()
	    @MethodOwner(owner = "qpsdemo")
	    public void testCreateUser() throws Exception {
	        LOGGER.info("test");
	        setCases("4555,54545");
	        PostUserMethod api = new PostUserMethod();
	        api.setProperties("api/users/user.properties");

	        AtomicInteger counter = new AtomicInteger(0);

	        api.callAPIWithRetry()
	                .withLogStrategy(APIMethodPoller.LogStrategy.ALL)
	                .peek(rs -> counter.getAndIncrement())
	                .until(rs -> counter.get() == 4)
	                .pollEvery(1, ChronoUnit.SECONDS)
	                .stopAfter(10, ChronoUnit.SECONDS)
	                .execute();
	        api.validateResponse();
	    }



}
