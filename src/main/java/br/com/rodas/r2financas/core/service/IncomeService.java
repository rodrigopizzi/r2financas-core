package br.com.rodas.r2financas.core.service;

import io.helidon.webserver.Routing.Rules;

import java.util.logging.Logger;

import br.com.rodas.r2financas.core.dao.IncomeDao;
import br.com.rodas.r2financas.core.domain.Income;
import io.helidon.common.http.Http;
import io.helidon.config.Config;
import io.helidon.webserver.Handler;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class IncomeService implements Service {

    private Logger logger = Logger.getLogger(IncomeService.class.getName());   
    
    private IncomeDao incomeDao;

    public IncomeService(Config config) {
        
	}

	@Override
    public void update(Rules rules) {
        rules.post("/", Handler.create(Income.class, this::create));
    }

    private void create(ServerRequest request, ServerResponse response, Income income) {
        logger.info(income.title);

        incomeDao.save(income);

        response.status(Http.Status.OK_200).send();
    }

}