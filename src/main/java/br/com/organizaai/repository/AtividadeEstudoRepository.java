package br.com.organizaai.repository;

import br.com.organizaai.model.AtividadeEstudoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AtividadeEstudoRepository extends JpaRepository<AtividadeEstudoModel, Long> {
    List<AtividadeEstudoModel> findAllByOrderByDataAscHorarioAsc();
    List<AtividadeEstudoModel> findByDataOrderByHorarioAsc(LocalDate data);
    long countByConcluidaTrue();
}
