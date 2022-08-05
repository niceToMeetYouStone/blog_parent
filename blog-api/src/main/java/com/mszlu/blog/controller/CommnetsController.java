package com.mszlu.blog.controller;


import com.mszlu.blog.service.CommentsService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.CommentParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("comments")
public class CommnetsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return  commentsService.commentByArticleId(id);
    }

    // create/change
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParams commentParams){
        log.info("控制层 commentParams： "+commentParams.toString());
        return commentsService.comment(commentParams);
    }
}
