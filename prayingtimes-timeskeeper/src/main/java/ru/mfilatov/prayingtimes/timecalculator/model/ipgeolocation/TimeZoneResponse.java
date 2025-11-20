package ru.mfilatov.prayingtimes.timecalculator.model.ipgeolocation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimeZoneResponse(
        @JsonProperty("time_zone")
        TimeZone timeZone
) {
    public record TimeZone(
            String name,
            int offset,
            @JsonProperty("offset_with_dst")
            int offsetWithDst,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDate date,

            @JsonProperty("date_time")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime dateTime,

            @JsonProperty("date_time_txt")
            String dateTimeTxt,

            @JsonProperty("date_time_wti")
            String dateTimeWti,

            @JsonProperty("date_time_ymd")
            String dateTimeYmd,

            @JsonProperty("date_time_unix")
            double dateTimeUnix,

            @JsonProperty("time_24")
            String time24,

            @JsonProperty("time_12")
            String time12,

            int week,
            int month,
            int year,

            @JsonProperty("year_abbr")
            String yearAbbr,

            @JsonProperty("is_dst")
            boolean isDst,

            @JsonProperty("dst_savings")
            int dstSavings,

            @JsonProperty("dst_exists")
            boolean dstExists
    ) {}
}