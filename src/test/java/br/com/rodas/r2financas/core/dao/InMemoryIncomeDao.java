package br.com.rodas.r2financas.core.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.rodas.r2financas.core.domain.Income;

public class InMemoryIncomeDao implements IncomeDao {

    private List<Income> incomes = new ArrayList<>();

    @Override
    public void save(Income income) {
        incomes.add(income);
    }

}