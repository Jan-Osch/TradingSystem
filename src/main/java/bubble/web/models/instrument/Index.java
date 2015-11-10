package bubble.web.models.instrument;


import bubble.web.commons.IdHelper;
import bubble.web.models.instrument.manager.StockManager;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Janusz.
 */
public class Index extends Instrument{

    public Index(String codeName, String fullName) {
        super(codeName, fullName, InstrumentType.INDEX);
    }
}
