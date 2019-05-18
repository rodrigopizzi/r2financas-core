package br.com.rodas.r2financas.core.domain;

import java.math.BigDecimal;

/**
 * Income record.
 */
public class Income {

    /**
     * This class constains all business logic possible exceptions for income
     * operations.
     */
    public class IncomeException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Permit construct with text of business logic.
         * @param errorMessage Cause of error
         */
        public IncomeException(final String errorMessage) {
            super(errorMessage);
        }
    }

    /** Primary key. */
    private long idIncome;

    /** Short description of transaction. */
    private String description;

    /** Value in money of transaction. */
    private BigDecimal value;

    /** Default constructor. */
    public Income() {
    }

    /**
     * Create new Income record.
     * @param id             unique id
     * @param newDescription Short description of transaction
     * @param newValue       Value in money of transaction
     * @throws IncomeException Occurs when cannot create a income record when
     *                         having errors in business logic
     */
    public Income(final long id, final String newDescription,
            final BigDecimal newValue) throws IncomeException {
        setIdIncome(id);
        setDescription(newDescription);
        setValue(newValue);
    }

    /**
     * @return Unique id for Icome record.
     */
    public long getIdIncome() {
        return idIncome;
    }

    /**
     * Get value in money of transaction.
     * @return value in money
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Set value in money of transaction.
     * @param newValue value in money
     * @throws IncomeException value must be positive
     */
    public void setValue(final BigDecimal newValue) throws IncomeException {
        if (newValue.doubleValue() < 0) {
            throw new IncomeException("The value of income must be positive.");
        }
        this.value = newValue;
    }

    /**
     * Get Short description of transaction.
     * @return text with description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set short description of transaction.
     * @param newDescription Must be text with description.
     * @throws IncomeException Description cannot be null value.
     */
    public void setDescription(final String newDescription)
            throws IncomeException {
        if (newDescription == null) {
            throw new IncomeException(
                    "The value of description cannot be null.");
        }
        this.description = newDescription;
    }

    /**
     * @param id unique id for Icome record
     */
    public void setIdIncome(final long id) {
        this.idIncome = id;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        final int next = 32;
        int result = 1;
        result = prime * result + (int) (idIncome ^ (idIncome >>> next));
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Income other = (Income) obj;
        if (idIncome != other.idIncome) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        return "Income [description=" + description + ", idIncome=" + idIncome
                + ", value=" + value + "]";
    }

}
