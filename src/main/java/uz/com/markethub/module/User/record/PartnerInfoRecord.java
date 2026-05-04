package uz.com.markethub.module.User.record;

public record PartnerInfoRecord(String code, String version) {

    @Override
    public String toString() {
        return "PartnerInfoRecord{" +
                "code='" + code + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
