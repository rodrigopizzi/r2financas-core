package br.com.rodas.r2financas.core.dao;

import java.sql.SQLException;
import br.com.rodas.r2financas.core.domain.Income;

/** Responsible for database access for Income records. */
public interface IncomeDao {
    /**
     * Create new Income record.
     * @param income Income record
     * @return New Income with new id
     * @throws SQLException Occurs when cannot save Income
     */
    Income save(Income income) throws SQLException;

    /**
     * Find Icome by ID.
     * @param idIncome Id of primary key
     * @return Icome record. Return null when not found icome by id.
     */
    Income findById(long idIncome);
}
