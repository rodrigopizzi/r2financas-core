package br.com.rodas.r2financas.core.dao;

import br.com.rodas.r2financas.core.domain.Income;

/** Responsible for database access for Income records. */
public interface IncomeDao {
    /**
     * Create new Income record.
     * @param income Income record
     */
    void save(Income income);
}
