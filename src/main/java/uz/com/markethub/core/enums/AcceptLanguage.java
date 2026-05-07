package uz.com.markethub.core.enums;

public enum AcceptLanguage {
    UZ, EN, RU;

    public static AcceptLanguage defaultLanguage(){
        return AcceptLanguage.UZ;
    }
}
