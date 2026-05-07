package uz.com.markethub.core.enums;

import lombok.Getter;

@Getter
public enum FileExtension {
    EXCEL("xlsx");

    private final String extension;

    FileExtension(String prefix) {
        this.extension = prefix;
    }
}
