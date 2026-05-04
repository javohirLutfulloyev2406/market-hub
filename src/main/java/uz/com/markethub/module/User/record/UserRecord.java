package uz.com.markethub.module.User.record;


import uz.com.markethub.module.User.domain.UserEntity;

public record UserRecord(String firstName, String lastName, String email) {

    public UserEntity mapToEntity() {
        return new UserEntity();
    }
}
