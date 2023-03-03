package dev.zontreck.essentials;

import dev.zontreck.libzontreck.chat.ChatColor;

public class Messages {
    public static final String ESSENTIALS_PREFIX;
    public static final String RTP_SEARCHING;
    public static final String RTP_CANCELLED;
    public static final String RTP_ABORTED;
    public static final String CONDITIONAL_RTP_ABORT;

    public static final String HOVER_WARP_INFO;
    public static final String WARP_OWNER;
    public static final String WARP_HOVER_FORMAT;
    public static final String WARP_RTP;
    public static final String COUNT;
    public static final String WARP_STANDARD;

    public static final String WARP_ACCESS_FORMAT;
    public static final String PUBLIC;
    public static final String PRIVATE;

    public static final String WARP_NAME_REQUIRED;
    public static final String WARP_NOT_EXIST;
    public static final String WARP_ATTEMPTING;
    public static final String WARPING;
    public static final String WARP_CREATED;
    public static final String WARP_CREATE_ERROR;
    public static final String WARP_RTP_CREATED;
    public static final String WARP_RTP_FOUND;
    public static final String WARP_DELETE_SUCCESS;
    public static final String WARP_DELETE_FAIL;

    public static final String TELEPORT_REQUEST_NOT_FOUND;
    public static final String TELEPORT_REQUEST_DENIED;
    public static final String TELEPORT_REQUEST_CANCELLED;

    static{
        ESSENTIALS_PREFIX = "!Gray![!Dark_Green!AE!Gray!] ";

        WARP_ATTEMPTING = ESSENTIALS_PREFIX + "!Dark_Green!Attempting to find a safe landing location. This may take a minute";
        WARPING = ESSENTIALS_PREFIX+"!Dark_green!Warping";

        WARP_RTP_FOUND = ESSENTIALS_PREFIX + "!Dark_Green!A suitable location has been found";
        RTP_SEARCHING = ESSENTIALS_PREFIX + "!Dark_Purple!Searching... Attempt !Gold![0]!White!/!Dark_Red![1]";
        RTP_CANCELLED = ESSENTIALS_PREFIX + "!Dark_Red!Last position was good, but another mod asked us not to send you there. This could happen with a claims mod.";
        RTP_ABORTED = ESSENTIALS_PREFIX + "!Dark_Red!Could not find a suitable location in [0] attempts. Giving up. [1]";
        CONDITIONAL_RTP_ABORT = "!Dark_Red!You may try again in !Gold![0] !Dark_Red!minutes and !Gold![1] !Dark_Red!second(s)";

        HOVER_WARP_INFO = "!Gold![Hover to see the Warp's info]";
        WARP_HOVER_FORMAT = "[0]\n[1]"; // 0 = owner, 1 = public infos
        WARP_RTP = "!Dark_Purple!This warp is a RTP. It will position you randomly in the dimension [0]";
        WARP_STANDARD = "!Green!This is a standard warp.";
        WARP_OWNER = "!Dark_Purple!The warp's owner is [0][1]";
        COUNT = ESSENTIALS_PREFIX + "!Dark_Purple!There are [0] [1](s) available";

        WARP_ACCESS_FORMAT = "!Dark_Purple!This warp is [0]";
        PUBLIC = "!Dark_Green!Public";
        PRIVATE = "!Dark_Red!Private";

        WARP_NAME_REQUIRED = ESSENTIALS_PREFIX + "!Dark_Red!The warp name is required in order to warp. You can click this message to get a full list of the warps you have access to.";
        WARP_NOT_EXIST = ESSENTIALS_PREFIX + "!Dark_Red!No Such Warp";

        WARP_CREATED = ESSENTIALS_PREFIX + "!Dark_Green!Warp created successfully";
        WARP_CREATE_ERROR = ESSENTIALS_PREFIX + "!Dark_Red!Warp could not be created due to [0]";

        WARP_RTP_CREATED = WARP_CREATED+" with RTP properties";


        WARP_DELETE_SUCCESS = ESSENTIALS_PREFIX + "!Dark_Green!Warp successfully deleted";
        WARP_DELETE_FAIL = ESSENTIALS_PREFIX + "!Dark_Red!Warp could not be deleted";


        TELEPORT_REQUEST_NOT_FOUND = ESSENTIALS_PREFIX + "!Dark_Red!The teleport request could not be found. Perhaps it already expired or was cancelled/denied already.";
        TELEPORT_REQUEST_DENIED = ESSENTIALS_PREFIX + "!Dark_Red!Teleport request was denied";
        TELEPORT_REQUEST_CANCELLED = ESSENTIALS_PREFIX + "!Dark_Red!Teleport request was cancelled";
    }
}
