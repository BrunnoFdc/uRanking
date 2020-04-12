package ga.brunnofdc.uranking.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringList extends ArrayList<String> implements List<String> {

    public StringList() {}

    public StringList(List<String> list) {
        super(list);
    }

    public StringList translateColors() {
        StringList translated = new StringList();
        forEach(line -> translated.add(ChatColor.translateAlternateColorCodes('&', line)));
        return translated;
    }

    public StringList replace(String from, String to) {
        StringList translated = new StringList();
        forEach(line -> translated.add(line.replace(from, to)));
        return translated;

    }

    public StringList replace(Map<String, String> replaces) {
        StringList translated = new StringList();
        forEach(line -> {
            replaces.forEach((key, value) -> {
                translated.add(line.replace(key, value));
            });
        });
        return translated;
    }

    @Override
    public String[] toArray() {
        String[] arr = new String[this.size()];
        int index = 0;
        for (String s : this) {
            arr[index] = s;
            index++;
        }
        return arr;
    }


}
