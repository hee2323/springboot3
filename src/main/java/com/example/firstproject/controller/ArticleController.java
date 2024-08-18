package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, Model model) {
        log.info(form.toString());
        // 1. DTO를 entity로 변환
        Article article = form.toEntity();
        log.info(article.toString());
        // 2. repository를 이용해 entity를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        model.addAttribute("db", saved.toString());
        return "redirect:/articles/" + saved.getId(); // 리다이렉트 넣어줄 위치
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {
        // 1. id를 조회해 데이터를 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 3. 뷰 페이지 반환하기
        return "/articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        // 1. DB에서 모든 Article 데이터 가져오기
        ArrayList<Article> articleEntityList = articleRepository.findAll();
        // 2. 가져온 Article 묶음을 모델에 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 사용자에게 보여줄 뷰 페이지 설정하기
        return "articles/index";
    }
}

