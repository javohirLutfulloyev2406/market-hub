package uz.com.markethub.core.enums;

import lombok.Getter;

@Getter
public enum FilePrefix {

    REPORT("report");

    private final String prefix;

    FilePrefix(String prefix) {
        this.prefix = prefix;
    }
}
