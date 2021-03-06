package eu.europeana.corelib.edm.exceptions;

import eu.europeana.corelib.web.exception.EuropeanaException;
import eu.europeana.corelib.web.exception.ProblemType;

/**
 * Exception that can be thrown when received data is invalid and/or inconsistent
 * @author Patrick Ehlert
 */
public class BadDataException extends EuropeanaException{

    private static final long serialVersionUID = -343856562570766816L;

    /**
     * @see eu.europeana.corelib.web.exception.EuropeanaException#EuropeanaException(ProblemType)
     */
    public BadDataException(ProblemType problem) {
        super(problem);
    }

    /**
     * @see eu.europeana.corelib.web.exception.EuropeanaException#EuropeanaException(ProblemType, String)
     */
    public BadDataException(ProblemType problem, String errorDetails) {
        super(problem, errorDetails);
    }

    /**
     * @see eu.europeana.corelib.web.exception.EuropeanaException#EuropeanaException(ProblemType, Throwable)
     */
    public BadDataException(ProblemType problem, Throwable causedBy) {
        super(problem, causedBy);
    }
}
