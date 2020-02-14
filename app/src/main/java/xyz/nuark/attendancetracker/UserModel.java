package xyz.nuark.attendancetracker;

import java.util.ArrayList;

/**
 * Created with love by Nuark on 14.02.2020.
 */
public class UserModel {
    private String name;
    private ArrayList<Integer> attended;

    public UserModel(String name) {
        this.name = name;
        attended = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttendance(int pos, UserStates state) {
        if (attended.size() <= pos) {
            attended.add(UserStates.UNKNOWN.ordinal());
        }
        attended.set(pos, state.ordinal());
    }

    public void removeAttendance(int pos) {
        if (attended.size() <= pos) {
            return;
        }
        attended.remove(pos);
    }

    public void addAttendance(UserStates state) {
        attended.add(state.ordinal());
    }

    public ArrayList<Integer> getAttended() {
        return attended;
    }

    public void setAttended(ArrayList<Integer> attended) {
        this.attended = attended;
    }

    public void clearAttendance() {
        this.attended.clear();
    }
}
