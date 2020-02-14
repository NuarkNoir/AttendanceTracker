package xyz.nuark.attendancetracker;

/**
 * Created with love by Nuark on 14.02.2020.
 */
public enum UserStates {
    MISSED, ATTENDED, UNKNOWN;

    public static UserStates getByOrdinal(int ordinal) {
        switch (ordinal) {
            default:
            case 0:
                return MISSED;
            case 1:
                return ATTENDED;
            case 2:
                return UNKNOWN;
        }
    }

    public static UserStates getNextState(UserStates state) {
        switch (state) {
            default:
            case UNKNOWN:
                return MISSED;
            case MISSED:
                return ATTENDED;
            case ATTENDED:
                return UNKNOWN;
        }
    }

    public static UserStates getStateFromString(String state) {
        switch (state.toLowerCase()) {
            default:
            case "unknown":
                return UNKNOWN;
            case "missed":
                return MISSED;
            case "attended":
                return ATTENDED;
        }
    }
}
