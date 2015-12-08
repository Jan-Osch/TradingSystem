package markets.entities.instrument;


/**
 * @author Janusz.
 */
public class Index extends Instrument{

    public Index(String codeName, String fullName) {
        super(codeName, fullName, InstrumentType.INDEX);
    }
}
