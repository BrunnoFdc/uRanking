package ga.brunnofdc.uranking.utils.enums;

public enum SingleLineMessage {

    NO_NEXT_RANK("No-Next-Rank"),
    NO_OLD_RANK("No-Old-Rank");

    private final String path;

    SingleLineMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
