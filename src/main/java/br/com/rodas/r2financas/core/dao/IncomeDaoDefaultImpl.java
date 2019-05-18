package br.com.rodas.r2financas.core.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.rodas.r2financas.core.DataSource;
import br.com.rodas.r2financas.core.domain.Income;

/**
 * Default implementation for IncomeDao.
 */
public class IncomeDaoDefaultImpl implements IncomeDao {

    /** Logger for this instance. */
    private static final Logger LOG =
            Logger.getLogger(IncomeDaoDefaultImpl.class.getName());

    @Override
    public final void save(final Income income) {
        final String insert =
                "insert into income" + " (idincome, description, value)"
                        + " values (nextval('seq_income'), ?, ?)";

        try (Connection con = DataSource.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(insert,
                    Statement.RETURN_GENERATED_KEYS)) {
                int parameterIndex = 1;
                ps.setString(parameterIndex++, income.getDescription());
                ps.setBigDecimal(parameterIndex++, income.getValue());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    long idIncome = rs.getLong(1);
                    income.setIdIncome(idIncome);
                }

                LOG.log(Level.INFO, "Created: " + income);
            }
        } catch (Exception e) {
            LOG.log(Level.INFO, income.toString());
            LOG.log(Level.SEVERE, "Cannot insert income record", e);
        }
    }

}
