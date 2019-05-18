package br.com.rodas.r2financas.core.service;

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
    private static final Logger LOG =
            Logger.getLogger(IncomeService.class.getName());

    /** Dao for Income. */
    private IncomeDao incomeDao = new IncomeDaoDefaultImpl();

    @Override
    public final void update(final Rules rules) {
        rules.post("/", Handler.create(Income.class, this::create));
    }

    /**
     * Create a new Income.
     * @param request  ServerRequest
     * @param response ServerResponse
     * @param income   Income operation
     */
    private void create(final ServerRequest request,
            final ServerResponse response, final Income income) {

        LOG.info(income.getDescription());
        incomeDao.save(income);
        response.status(Http.Status.OK_200).send();

    }

}
