package br.com.organizaai.repository;

import br.com.organizaai.model.AtividadeEstudo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AtividadeEstudoRepository extends JpaRepository<AtividadeEstudo, Long> {
    List<AtividadeEstudo> findAllByOrderByDataAscHorarioAsc();
    List<AtividadeEstudo> findByDataOrderByHorarioAsc(LocalDate data);
    long countByConcluidaTrue();
}
