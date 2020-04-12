package ga.brunnofdc.uranking.utils.enums;

public enum Property {

    /* System Settings */
    USE_MYSQL("System-Settings.Database.MySQL.Use"),

    /* Behaviour */
    PM_RANKUP("System-Behaviour.PM-On-Rankup"),
    BROADCAST_RANKUP("System-Behaviour.Broadcast-On-Rankup"),
    PM_MAXRANK("System-Behaviour.PM-On-Max-Rank"),
    BROADCAST_MAXRANK("System-Behaviour.Broadcast-On-Max-Rank"),
    SEND_TITLE_ENABLE("System-Behaviour.Send-Title.Enable"),
    SEND_TITLE_GLOBAL("System-Behaviour.Send-Title.Global"),
    RANKUP_GUI_ENABLE("Rankup-GUI.Enable");

    public final String configPath;

    Property(String path) {
        this.configPath = path;
    }


}
