package br.com.kafkamanager.domain.exceptions;

import br.com.kafkamanager.domain.validation.Error;
import java.util.List;
import java.util.stream.Collectors;

public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anErrors) {
        return new DomainException(anErrors.getMessage(), List.of(anErrors));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        return errors.stream().map(Error::getMessage).collect(Collectors.joining(", "));
    }
}
