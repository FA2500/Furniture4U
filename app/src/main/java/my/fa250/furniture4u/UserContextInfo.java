package my.fa250.furniture4u;

public class UserContextInfo {
    private static String primaryColours;
    private static String[] objects;

    public static String getPrimaryColours() {
        return primaryColours;
    }

    public static void setPrimaryColours(String primaryColours) {
        UserContextInfo.primaryColours = primaryColours;
    }

    public static String[] getObjects() {
        return objects;
    }

    public static void setObjects(String[] objects) {
        UserContextInfo.objects = objects;
    }

}
