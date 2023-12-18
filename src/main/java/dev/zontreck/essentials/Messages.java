package dev.zontreck.essentials;

import dev.zontreck.libzontreck.chat.ChatColor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

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

    public static final String TELEPORT_ACCEPT;
    public static final String TELEPORT_DENY;
    public static final SoundEvent TPA_SOUND;
    public static final String TPA_HERE;

    public static final String PLAYER_NOT_FOUND;
    public static final String NO_TP_TO_SELF;
    public static final String NO_MORE_THAN_ONE_TPA;
    public static final String TPA;

    public static final String HOME_FORMAT;
    public static final String HOME_HOVER_TEXT;
    public static final String HOME_COUNT;

    public static final String TELEPORT_REQUEST_ACCEPTED;

    public static final String TELEPORTING_HOME;
    public static final String TELEPORT_HOME_FAIL;

    public static final String HOME_DELETE_SUCCESS;
    public static final String HOME_DELETE_FAIL;

    public static final String HOME_CREATE_SUCCESS;
    public static final String HOME_CREATE_FAIL;

    public static final String PAYMENT_ATTEMPTING;
    public static final String PAYMENT_FAILED;
    public static final String PAYMENT_SUCCESS;

    public static final String HEARTS_USAGE;
    public static final String HEARTS_UPDATED;
    public static final String RESPAWNING;


    static{
        ESSENTIALS_PREFIX = "!Gray![!Dark_Green!AE!Gray!] ";

        WARP_ATTEMPTING = ESSENTIALS_PREFIX + "!Dark_Green!Attempting to find a safe landing location. This may take a minute";
        WARPING = ESSENTIALS_PREFIX+"!Dark_Green!Warping";

        WARP_RTP_FOUND = ESSENTIALS_PREFIX + "!Dark_Green!A suitable location has been found";
        RTP_SEARCHING = ESSENTIALS_PREFIX + "!Dark_Purple!Searching... Attempt !Gold![0]!White!/!Dark_Red![1]";
        RTP_CANCELLED = ESSENTIALS_PREFIX + "!Dark_Red!Last position was good, but another mod asked us not to send you there. This could happen with a claims mod.";
        RTP_ABORTED = ESSENTIALS_PREFIX + "!Dark_Red!Could not find a suitable location in [0] attempts. Giving up. [1]";
        CONDITIONAL_RTP_ABORT = "!Dark_Red!You may try again in !Gold![0] !Dark_Red!minutes and !Gold![1] !Dark_Red!second(s)";

        HOVER_WARP_INFO = " !Gold![Hover to see the Warp's info]";
        WARP_HOVER_FORMAT = "[0] \n[1]"; // 0 = owner, 1 = public infos
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
        TELEPORT_REQUEST_ACCEPTED = ESSENTIALS_PREFIX + "!Dark_Green!Teleport request was accepted";

        TPA_SOUND = SoundEvents.ANVIL_FALL;
        TELEPORT_ACCEPT = "!Dark_Gray![!Dark_Green!Accept!Dark_Gray!]";
        TELEPORT_DENY = "!Dark_Gray![!Dark_Red!Deny!Dark_Gray!]";

        TPA_HERE = "[0] !Bold!!Dark_Purple!is requesting you to teleport to them!\n\n";

        PLAYER_NOT_FOUND = ESSENTIALS_PREFIX + "!Dark_Red!Error: Player not found";
        NO_TP_TO_SELF = ESSENTIALS_PREFIX + "!Dark_Red!You cannot teleport to yourself!";
        NO_MORE_THAN_ONE_TPA = ESSENTIALS_PREFIX + "!Dark_Red!You already have a TPA Request active, wait for it to expire, or use the cancel button/command";

        TPA = "[0] !Bold!!Dark_Purple! is requesting to teleport to you!\n\n";

        HOME_FORMAT = "!Dark_Gray![!Gold!Home!Dark_Gray!] !Bold!!Dark_Purple![0]";
        HOME_HOVER_TEXT = "!Bold!!Dark_Green!Click here to go to this home";
        HOME_COUNT = ESSENTIALS_PREFIX + "!Bold!!Dark_Purple!You have [0] total homes.";

        TELEPORTING_HOME = ESSENTIALS_PREFIX + "!Dark_Green!Teleporting home now!";
        TELEPORT_HOME_FAIL = ESSENTIALS_PREFIX + "!Dark_Red!Home not found";

        HOME_DELETE_FAIL = ESSENTIALS_PREFIX + "!Dark_Red!Home could not be deleted due to an unknown error";
        HOME_DELETE_SUCCESS = ESSENTIALS_PREFIX + "!Dark_Green!Home was successfully deleted";

        HOME_CREATE_SUCCESS = ESSENTIALS_PREFIX + "!Dark_Green!Home was created or updated successfully";
        HOME_CREATE_FAIL = ESSENTIALS_PREFIX + "!Dark_Red!Home could not be created or updated";

        PAYMENT_ATTEMPTING = ESSENTIALS_PREFIX + "!Dark_Green!Please wait... Attempting to pay [0] to [1]";
        PAYMENT_FAILED = ESSENTIALS_PREFIX + "!Dark_Red!Payment failed";
        PAYMENT_SUCCESS = ESSENTIALS_PREFIX + "!Dark_Green!Payment successful";

        HEARTS_USAGE = ESSENTIALS_PREFIX + "!Dark_Green!This command allows you to toggle on and off the compressed hearts feature without editing the config file.\n\n!Gold!/hearts [true/false]";

        HEARTS_UPDATED = ESSENTIALS_PREFIX + "!Dark_Red!Your hearts preferences have been updated";

        RESPAWNING = ESSENTIALS_PREFIX + "!Dark_Green!Respawning at World Spawn";
    }
}
