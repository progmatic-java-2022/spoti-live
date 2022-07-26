package hu.progmatic.spotilive.felhasznalo;


import java.util.Arrays;

public enum UserType {
    ADMIN(
        Roles.ADMIN_ROLE,
            Roles.USER_WRITE_ROLE,
            Roles.USER_READ_ROLE,
            Roles.ESEMENY_KEZELES_ROLE,
            Roles.ZENEKAR_KEZELES_ROLE,
            Roles.ZENE_KEZELES_ROLE,
            Roles.TAG_KARBANTARTAS_ROLE,
            Roles.USER_CREATE_ROLE,
            Roles.NEWGUEST_CREATE_ROLE
    ),
    USER(
            Roles.USER_READ_ROLE
    ),
    ZENEKAR(
            Roles.ESEMENY_KEZELES_ROLE,
            Roles.ZENE_KEZELES_ROLE,
            Roles.NEWGUEST_CREATE_ROLE
    ),
    GUEST(Roles.GUEST_READ_ROLE);


    private final String[] roles;


    UserType(String... roles) {
        this.roles = roles;
    }

    public String[] getRoles() {
        return roles;
    }

    public boolean hasRole(String role) {
        return Arrays.asList(roles).contains(role);
    }

    public static class Roles {
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String USER_WRITE_ROLE = "USER_WRITE";
        public static final String USER_READ_ROLE = "USER_READ";
        public static final String ESEMENY_KEZELES_ROLE = "ESEMENY_KEZELES";
        public static final String ZENEKAR_KEZELES_ROLE = "ZENEKAR_KEZELES";
        public static final String ZENE_KEZELES_ROLE = "ZENE_KEZELES";
        public static final String TAG_KARBANTARTAS_ROLE = "TAG_KARBANTARTAS";
        public static final String GUEST_READ_ROLE = "GUEST_READ";
        public static final String USER_CREATE_ROLE = "USER_CREATE";
        public static final String NEWGUEST_CREATE_ROLE = "NEWGUEST_CREATE";
    }


}
