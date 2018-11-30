package org.riversoft.salt.gui.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class DateTimeParser {

    static String[] formatStrings = ["MM/dd/yyyy HH:mm", "dd.MM.yyyy HH:mm", "dd.MM.yyyy"];

    /**
     * Метод предназначен для преобразования строки в дату
     * @param dateString дата в виде строки
     * @return преобразованую строку в виде даты
     */
    static Date dateParse(String dateString) {

        for (String formatString : formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            }
            catch (ParseException e) {
            }
        }

        return null;
    }

    /**
     * Метод предназначен для преобразования даты в форматированную строку
     * @param date дата которуб необходимо преобразовать
     * @return отформатированную дату в виде строки
     */
    static String dateToString(Date date) {

        if (date) {

            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            String dateString = df.format(date);

            return dateString
        } else {
            return null
        }
    }
}
