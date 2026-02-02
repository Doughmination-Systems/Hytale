package win.doughmination.plural.data;

import java.util.HashMap;
import java.util.Map;

public class PlayerSystem {
    private Map<String, ProxyMember> members = new HashMap<>();
    private String currentFront = null;

    public void addMember(String name, ProxyMember member) {
        members.put(name.toLowerCase(), member);
    }

    public void removeMember(String name) {
        String lowerName = name.toLowerCase();
        if (currentFront != null && currentFront.equalsIgnoreCase(lowerName)) {
            currentFront = null;
        }
        members.remove(lowerName);
    }

    public ProxyMember getMember(String name) {
        return members.get(name.toLowerCase());
    }

    public boolean hasMember(String name) {
        return members.containsKey(name.toLowerCase());
    }

    public Map<String, ProxyMember> getMembers() {
        return members;
    }

    public void setFront(String memberName) {
        this.currentFront = memberName.toLowerCase();
    }

    public String getCurrentFront() {
        return currentFront;
    }

    public ProxyMember getFrontingMember() {
        if (currentFront == null) {
            return null;
        }
        return members.get(currentFront);
    }

    public void clearFront() {
        this.currentFront = null;
    }
}