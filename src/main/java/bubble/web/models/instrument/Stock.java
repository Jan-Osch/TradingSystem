package bubble.web.models.instrument;

public class Stock extends Instrument{
    public Stock(String codeName, String fullName) {
        super(codeName, fullName, InstrumentType.STOCK);
    }


}
