package ru.artempugachev.homeinformation.weather;

import ru.artempugachev.homeinformation.R;

public class Wind {
    private double speed;
    private int direction;

    public Wind(double speed, int direction) {
        this.speed = speed;
        this.direction = direction;
    }

    /**
     * Get wind arrow by wind direction
     * 338 <= "N" <= 23
     * 24 <= "NE" <= 67
     * 68 <= "E" <= 112
     * 113 <=  "SE" <= 157
     * 158 <= "S" <= 202
     * 203 <=  "SW" <= 247
     * 248 <= "W" <= 292
     * 293 <= "NW" <= 337
     * */
    public int toWindArrowResource() {
        final int DIR_MIN = 0;
        final int DIR_MAX = 359;

        final int NORTH_MIN = 338;
        final int NORTH_MAX = 23;
        final int NORTH_EAST_MIN = 24;
        final int NORTH_EAST_MAX = 67;
        final int EAST_MIN = 68;
        final int EAST_MAX = 112;
        final int SOUTH_EAST_MIN = 113;
        final int SOUTH_EAST_MAX = 157;
        final int SOUTH_MIN = 158;
        final int SOUTH_MAX = 202;
        final int SOUTH_WEST_MIN = 203;
        final int SOUTH_WEST_MAX = 247;
        final int WEST_MIN = 248;
        final int WEST_MAX = 292;
        final int NORTH_WEST_MIN = 293;
        final int NORTH_WEST_MAX = 337;

        final int windArrowResource;

        if (DIR_MIN <= direction && direction <= NORTH_MAX ) {
            windArrowResource = R.drawable.wind_north;
        } else if (NORTH_EAST_MIN <= direction && direction <= NORTH_EAST_MAX) {
            windArrowResource = R.drawable.wind_north_east;
        } else if (EAST_MIN <= direction && direction <= EAST_MAX) {
            windArrowResource = R.drawable.wind_east;
        } else if (SOUTH_EAST_MIN <= direction && direction <= SOUTH_EAST_MAX) {
            windArrowResource = R.drawable.wind_south_east;
        } else if (SOUTH_MIN <= direction && direction <= SOUTH_MAX) {
            windArrowResource = R.drawable.wind_south;
        } else if (SOUTH_WEST_MIN <= direction && direction <= SOUTH_WEST_MAX) {
            windArrowResource = R.drawable.wind_south_west;
        } else if (WEST_MIN <= direction && direction <= WEST_MAX) {
            windArrowResource = R.drawable.wind_west;
        } else if (NORTH_WEST_MIN <= direction && direction <= NORTH_WEST_MAX) {
            windArrowResource = R.drawable.wind_north_west;
        } else if (NORTH_MIN <= direction && direction <= DIR_MAX) {
            windArrowResource = R.drawable.wind_north;
        } else {
            windArrowResource = R.drawable.wind_north;
        }

        return windArrowResource;
    }
}
