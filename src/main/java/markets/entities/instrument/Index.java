package markets.entities.instrument;

public class Index extends Instrument {

    public Index(String codeName, String fullName) {
        super(codeName, fullName, InstrumentType.INDEX);
    }
}
