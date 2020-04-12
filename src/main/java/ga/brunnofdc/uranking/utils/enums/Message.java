package ga.brunnofdc.uranking.utils.enums;

public enum Message {

    RANKUP_PM("Rankup-Success"),
    RANKUP_ANNOUNCE("Rankup-Success-Announce"),
    RANKUP_MAX_PM("Rankup-Max-Rank"),
    RANKUP_MAX_ANNOUNCE("Rankup-Max-Rank-Announce"),
    NO_MONEY("Insufficient-Money"),
    NO_PERMISSION("No-Permission"),
    ALREADY_MAX_RANK("Already-Max-Rank"),
    RANK_NOT_STORED("Rank-Not-Stored"),
    RANKUP_CANCELED("Rankup-Canceled");


    private final String path;

    Message(String path) {
        this.path = path;
    }


    public String getPath() {
        return this.path;
    }

}
