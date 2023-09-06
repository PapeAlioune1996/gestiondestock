package com.sn.gestionstock.dao;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Article;

@Repository
public interface ArticleDao extends JpaRepository<Article, Integer> {

	  Optional<Article> findArticleByCodeArticle(String codeArticle);

	  List<Article> findAllByCategoryId(Integer idCategory);
}
