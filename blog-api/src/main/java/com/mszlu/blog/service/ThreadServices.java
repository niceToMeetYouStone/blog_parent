package com.mszlu.blog.service;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mszlu.blog.dao.mapper.ArticleMapper;
import com.mszlu.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ThreadServices {

    // 希望这个操作在线程池中执行不会影响主线程
    @Async("taskExecutor")
    public void updateArtileViewCont(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(Article::getId,article.getId());
        // 设置一个 为了在多线程的情况下 线程安全
        updateWrapper.eq(Article::getViewCounts,article.getViewCounts());
        articleMapper.update(articleUpdate,updateWrapper);

    }
}
