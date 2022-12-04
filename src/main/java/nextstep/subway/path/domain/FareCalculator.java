package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public class FareCalculator {
    private static final int MAX_DEFAULT_DISTANCE = 10;
    private static final int MAX_MIDDLE_DISTANCE = 50;
    private static final Integer DEFAULT_FARE = 1250;
    private static final int MIDDLE_SURCHARGE_CONDITION = 5;
    private static final int LONG_SURCHARGE_CONDITION = 8;
    private static final int SURCHARGE = 100;

    private FareCalculator() {

    }

    public static Integer calculateFare(Distance distance) {
        if (distance.isDefaultDistance()) {
            return calculateDefaultFare();
        }
        if (distance.isMiddleDistance()) {
            return calculateMiddleFare(distance.getDistance());
        }
        if (distance.isLongDistance()) {
            return calculateLongFare(distance.getDistance());
        }
        return 0;
    }

    private static Integer calculateDefaultFare() {
        return DEFAULT_FARE;
    }

    private static Integer calculateMiddleFare(int distance) {
        int fare = DEFAULT_FARE;
        int middleDistance = distance - MAX_DEFAULT_DISTANCE;
        fare += ((int) ((Math.ceil((middleDistance - 1) / MIDDLE_SURCHARGE_CONDITION) + 1) * SURCHARGE));

        return fare;
    }

    private static Integer calculateLongFare(int distance) {
        int fare = 0;
        int longDistance = distance - MAX_MIDDLE_DISTANCE;
        fare += calculateMiddleFare(MAX_MIDDLE_DISTANCE);
        fare += ((int) ((Math.ceil((longDistance - 1) / LONG_SURCHARGE_CONDITION) + 1) * SURCHARGE));

        return fare;
    }
}