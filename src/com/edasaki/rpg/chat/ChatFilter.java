package com.edasaki.rpg.chat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import com.edasaki.core.options.SakiOption;
import com.edasaki.rpg.PlayerDataRPG;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ChatFilter {

    private static final String[] FILTERED_WORDS = {
            "anal",
            "ballsack",
            "bastard",
            "bastards",
            "bitch",
            "bitches",
            "bitchs",
            "blowjob",
            "blowjobs",
            "boner",
            "boners",
            "boob",
            "boobs",
            "asshole",
            "assholes",
            "clit",
            "cunt",
            "damn",
            "dick",
            "dildo",
            "dildos",
            "fag",
            "fags",
            "faggot",
            "faggots",
            "fellatio",
            "fuck",
            "fucks",
            "fucked",
            "fucku",
            "fuckd",
            "fucker",
            "fucking",
            "fuckin",
            "damnit",
            "gay",
            "gays",
            "lesbian",
            "lesbians",
            "goddamn",
            "hell",
            "hellhole",
            "jackass",
            "jizz",
            "labia",
            "muff",
            "nigger",
            "niggers",
            "penis",
            "penises",
            "piss",
            "queer",
            "scrotum",
            "shit",
            "shits",
            "shitter",
            "slut",
            "sluts",
            "smegma",
            "tit",
            "titty",
            "tittie",
            "titties",
            "turd",
            "vagina",
            "wank",
            "whore"
    };

    private static final String CENSOR = "**************************************************";

    private static LoadingCache<String, String> filteredCache;

    static {
        filteredCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(10, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
            public String load(String key) throws Exception {
                return filter(key);
            }
        });
    }

    private static String filter(String original) {
        StringBuilder sb = new StringBuilder();
        for (String s : original.split(" ")) {
            boolean filtered = false;
            for (String f : FILTERED_WORDS) {
                if (s.equalsIgnoreCase(f)) {
                    sb.append(CENSOR.substring(0, s.length()));
                    filtered = true;
                    break;
                }
            }
            if (!filtered)
                sb.append(s);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static String getFiltered(String s) {
        String filtered;
        try {
            filtered = filteredCache.get(s);
            return filtered;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return s;
        }
    }

    public static void sendMessage(Player p, PlayerDataRPG pd, String s) {
        if (pd.getOption(SakiOption.CHAT_FILTER)) {
            p.sendMessage(getFiltered(s));
        } else {
            p.sendMessage(s);
        }
    }
}
