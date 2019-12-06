package enums;

public enum DocumentType{
	MEDICAL_REPORT(0, (byte)0x00),
	OATH(1, (byte)0x01),
	CSV(2, (byte)0x02);

    public final int documentType;
    public final byte bit;

    DocumentType(int documentType, byte bit){
        this.documentType = documentType;
        this.bit = bit;
    }
}
