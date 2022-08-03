package com.mszlu.blog.controller;


import com.mszlu.blog.service.ArticleService;
import com.mszlu.blog.vo.Result;
import com.mszlu.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



//json数据进行交互
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams){
         return articleService.listArticle(pageParams);
    }


    @PostMapping("hot")
    public  Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }
    @PostMapping("new")
   public  Result newAtricle(){
        int limit  = 5;
        return articleService.newArticle(limit);
   }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
   public Result listArchives(){
        return articleService.listArchives();
   }
}
