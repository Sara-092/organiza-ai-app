package br.com.organizaai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.organizaai.model.Flashcard;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findAllByOrderByIdDesc();
}