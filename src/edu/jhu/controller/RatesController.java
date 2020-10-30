package edu.jhu.controller;

import edu.jhu.model.BookingDay;
import edu.jhu.model.Rates;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class serves as a controller between the rates and the server (server)
 */
public class RatesController {
    // stores local copy of data passed from client
    private final String DATA;
    // splits data into integer items
    private int[] data_items;
    // holds booking date
    private BookingDay booking_day;

    /**
     * Public constructor
     *
     * @param DATA data from controller
     */
    public RatesController(String DATA) {
        // creates local reference of data
        this.DATA = DATA;
    }

    /**
     * Method used to get the rates result
     *
     * @return price or error message
     */
    public String getResult() {
        // validate if data has valid pattern (1:2008:7:1:3)
        if (!validPattern()) {
            return error("Invalid date. You should provide the date in the form of 1:2008:7:1:3");
        }
        // validate if data is a valid date
        else if (!validDate()) {
            return error("Invalid date. Check the date and try again");
        }
        // perform rate calculation
        else {
            return rate();
        }
    }

    /**
     * Method used to validate if data matches regular expression
     * create regular expression to validate if data follows  (ID:Year:Month:Day:Duration)
     * ID ->        Any number between 1 and 3
     * Year ->      In the format YYYY
     * Month ->     Any number between 1 and 12
     * Day ->       Any number between 1 and 31
     * Duration ->  Any number between 2 and 7
     *
     * @return returns true if data match regex, otherwise returns false
     */
    private boolean validPattern() {
        // regular expression
        final Pattern PATTERN = Pattern.compile("0?[0-2]:[0-9]{4}:(0?[1-9]|1[0-2]?):(0?[1-9]|[12]\\d|3[01]):(0?[2-7])");
        // compare regex and data
        final Matcher MATCHER = PATTERN.matcher(this.DATA);
        // return match result
        return MATCHER.matches();
    }

    /**
     * Method used to validate if a date is valid using the BookingDate class
     *
     * @return true if date is valid otherwise returns false
     */
    private boolean validDate() {
        // Split string and store in array
        this.data_items = Arrays.stream(this.DATA.split(":"))
                .mapToInt(Integer::parseInt)
                .toArray();

        // set indexes to get corresponding values form data items array
        final int YEAR = 1;
        final int MONTH = 2;
        final int DAY = 3;

        // pass values to Booking Date
        this.booking_day = new BookingDay(
                data_items[YEAR],
                data_items[MONTH],
                data_items[DAY]);

        // validate if date is valid
        return booking_day.isValidDate();
    }

    /**
     * Method used to calculate hike rate
     *
     * @return rate or error message
     */
    private String rate() {

        // set indexes to get corresponding values form data items array
        final int ID = 0;
        final int DURATION = 4;

        // instantiate Rates and passe ID and duration
        Rates rates = new Rates(Rates.HIKE.values()[data_items[ID]]);
        rates.setBeginDate(this.booking_day);
        boolean validDuration = rates.setDuration(data_items[DURATION]);
        boolean validDates = rates.isValidDates();

        // if duration is not valid return error
        if (!validDuration) {
            return error(String.format("Invalid hike duration. (Valid hike durations: %s days) E.M.",
                    Arrays.toString(rates.getDurations())));
        }
        // if hike date is invalid return error
        else if (!validDates) {
            return error(String.format("Invalid hike dates. %s [Season starts: %d\\%d, Season end: %d\\%d] E.M",
                    rates.getDetails(),
                    rates.getSeasonStartMonth(),
                    rates.getSeasonStartDay(),
                    rates.getSeasonEndMonth(),
                    rates.getSeasonEndDay()));
        }
        // calculate rate
        else {
            // return string value of cost
            return rates.getCost() + ":Quoted Rate";
        }
    }

    /**
     * method used to format error messages
     *
     * @param message error message
     * @return formatted error message
     */
    private String error(String message) {
        // create error message
        return "-0.01:" + message;
    }
}
