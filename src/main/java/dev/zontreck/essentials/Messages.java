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
    
    static{
        ESSENTIALS_PREFIX = "!Gray![!Dark_Green!AE!Gray!] ";

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

        WARP_ACCESS_FORMAT = "This warp is [0]";
    }
}
