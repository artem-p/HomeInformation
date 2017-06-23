package ru.artempugachev.myweather.weather

import ru.artempugachev.myweather.R

class Wind(val speed: Double, val direction: Int) {
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
    fun toWindArrowResource(): Int {
        val DIR_MIN = 0
        val DIR_MAX = 359

        val NORTH_MIN: Int = 338
        val NORTH_MAX: Int = 23
        val NORTH_EAST_MIN = 24
        val NORTH_EAST_MAX = 67
        val EAST_MIN = 68
        val EAST_MAX = 112
        val SOUTH_EAST_MIN = 113
        val SOUTH_EAST_MAX = 157
        val SOUTH_MIN = 158
        val SOUTH_MAX = 202
        val SOUTH_WEST_MIN = 203
        val SOUTH_WEST_MAX = 247
        val WEST_MIN = 248
        val WEST_MAX = 292
        val NORTH_WEST_MIN = 293
        val NORTH_WEST_MAX = 337

        val windArrowResource: Int

        when (direction) {
            in DIR_MIN..NORTH_MAX -> windArrowResource = R.drawable.wind_north
            in NORTH_EAST_MIN..NORTH_EAST_MAX -> windArrowResource = R.drawable.wind_north_east
            in EAST_MIN..EAST_MAX -> windArrowResource = R.drawable.wind_east
            in SOUTH_EAST_MIN..SOUTH_EAST_MAX -> windArrowResource = R.drawable.wind_south_east
            in SOUTH_MIN..SOUTH_MAX -> windArrowResource = R.drawable.wind_south
            in SOUTH_WEST_MIN..SOUTH_WEST_MAX -> windArrowResource = R.drawable.wind_south_west
            in WEST_MIN..WEST_MAX -> windArrowResource = R.drawable.wind_west
            in NORTH_WEST_MIN..NORTH_WEST_MAX -> windArrowResource = R.drawable.wind_north_west
            in NORTH_MIN..DIR_MAX -> windArrowResource = R.drawable.wind_north
            else -> windArrowResource = R.drawable.wind_north
        }

        return windArrowResource
    }
}
