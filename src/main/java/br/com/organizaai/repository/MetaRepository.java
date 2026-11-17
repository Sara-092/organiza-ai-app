package br.com.organizaai.repository;

import br.com.organizaai.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {

    List<Meta> findAllByOrderByConcluidaAscDataLimiteAsc();

    long countByConcluida(boolean concluida);
}