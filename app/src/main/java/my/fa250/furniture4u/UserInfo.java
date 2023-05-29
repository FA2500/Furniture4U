package my.fa250.furniture4u;

public class UserInfo {

    private static String name;
    private static String email;
    private static String phone;
    private static String role;

    private static String provider;

    private static String token;

    public static String getName()
    {
        return name;
    }

    public static void setName(String a)
    {
        name = a;
    }

    public static String getEmail()
    {
        return email;
    }

    public static void setEmail(String a)
    {
        email = a;
    }

    public static String getPhone()
    {
        return phone;
    }

    public static void setPhone(String a)
    {
        phone = a;
    }

    public static String getRole()
    {
        return role;
    }

    public static void setRole(String a)
    {
        role = a;
    }

    public static String getProvider() {
        return provider;
    }

    public static void setProvider(String provider) {
        UserInfo.provider = provider;
    }

    public static String getToken() { return token ;}

    public static void setToken(String token) {UserInfo.token = token;}
}
