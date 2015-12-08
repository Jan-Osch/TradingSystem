package markets.entities.instrument;


public abstract class Instrument {
    private String codeName;
    private String fullName;
    private InstrumentType instrumentType;

    public Instrument(String codeName, String fullName, InstrumentType instrumentType) {
        this.codeName = codeName;
        this.fullName = fullName;
        this.instrumentType = instrumentType;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
