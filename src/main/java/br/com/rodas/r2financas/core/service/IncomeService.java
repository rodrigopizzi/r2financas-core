package br.com.rodas.r2financas.core.service;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.com.rodas.r2financas.core.dao.IncomeDao;
import br.com.rodas.r2financas.core.dao.IncomeDaoDefaultImpl;
import br.com.rodas.r2financas.core.domain.Income;
import io.helidon.common.http.Http;
import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing.Rules;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

/**
 * Income Service. Responsible for create, update and delete income records
 */
public class IncomeService implements Service {

    /** Logger for this instance. */
    private static final Logger LOG = Logger.getLogger(IncomeService.class.getName());

    @Override
    public final void update(final Rules rules) {
        rules.post("/", Handler.create(Income.class, this::create)).get("/{id}", this::findById);
    }

    /**
     * Create a new Income.
     * 
     * @param request  ServerRequest
     * @param response ServerResponse
     * @param income   Income operation
     */
    private void create(final ServerRequest request, final ServerResponse response,
            final Income income) {

        try {
            IncomeDao incomeDao = new IncomeDaoDefaultImpl();
            Income newIncome = incomeDao.save(income);
            response.status(Http.Status.OK_200);
            response.send(newIncome);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Cannot insert Income", e);
            response.status(Http.Status.INTERNAL_SERVER_ERROR_500).send();
        }
    }

    /**
     * Find Income by ID.
     * 
     * @param request  ServerRequest
     * @param response ServerResponse
     */
    private void findById(final ServerRequest request, final ServerResponse response) {
        IncomeDao incomeDao = new IncomeDaoDefaultImpl();

        String id = request.path().param("id");
        long idIncome = Long.parseLong(id);

        Income income = incomeDao.findById(idIncome);
        response.send(income);
    }

}
