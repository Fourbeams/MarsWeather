package com.fourbeams.marsweather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POJO {

    public class ReportResponse{

        @SerializedName("report")
        @Expose
        private Report report;

        public Report getReport() {
            return report;
        }

        public void setReport(Report report) {
            this.report = report;
        }
    }

    public class Report {
        @SerializedName("terrestrial_date")
        @Expose
        private String terrestrialDate;
        @SerializedName("sol")
        @Expose
        private Integer sol;
        @SerializedName("ls")
        @Expose
        private Double ls;
        @SerializedName("min_temp")
        @Expose
        private Double minTemp;
        @SerializedName("min_temp_fahrenheit")
        @Expose
        private Double minTempFahrenheit;
        @SerializedName("max_temp")
        @Expose
        private Double maxTemp;
        @SerializedName("max_temp_fahrenheit")
        @Expose
        private Double maxTempFahrenheit;
        @SerializedName("pressure")
        @Expose
        private Double pressure;
        @SerializedName("pressure_string")
        @Expose
        private String pressureString;
        @SerializedName("abs_humidity")
        @Expose
        private Object absHumidity;
        @SerializedName("wind_speed")
        @Expose
        private Object windSpeed;
        @SerializedName("wind_direction")
        @Expose
        private String windDirection;
        @SerializedName("atmo_opacity")
        @Expose
        private String atmoOpacity;
        @SerializedName("season")
        @Expose
        private String season;
        @SerializedName("sunrise")
        @Expose
        private String sunrise;
        @SerializedName("sunset")
        @Expose
        private String sunset;

        public String getTerrestrialDate() {
            return terrestrialDate;
        }

        public void setTerrestrialDate(String terrestrialDate) {
            this.terrestrialDate = terrestrialDate;
        }

        public Integer getSol() {
            return sol;
        }

        public void setSol(Integer sol) {
            this.sol = sol;
        }

        public Double getLs() {
            return ls;
        }

        public void setLs(Double ls) {
            this.ls = ls;
        }

        public Double getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(Double minTemp) {
            this.minTemp = minTemp;
        }

        public Double getMinTempFahrenheit() {
            return minTempFahrenheit;
        }

        public void setMinTempFahrenheit(Double minTempFahrenheit) {
            this.minTempFahrenheit = minTempFahrenheit;
        }

        public Double getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(Double maxTemp) {
            this.maxTemp = maxTemp;
        }

        public Double getMaxTempFahrenheit() {
            return maxTempFahrenheit;
        }

        public void setMaxTempFahrenheit(Double maxTempFahrenheit) {
            this.maxTempFahrenheit = maxTempFahrenheit;
        }

        public Double getPressure() {
            return pressure;
        }

        public void setPressure(Double pressure) {
            this.pressure = pressure;
        }

        public String getPressureString() {
            return pressureString;
        }

        public void setPressureString(String pressureString) {
            this.pressureString = pressureString;
        }

        public Object getAbsHumidity() {
            return absHumidity;
        }

        public void setAbsHumidity(Object absHumidity) {
            this.absHumidity = absHumidity;
        }

        public Object getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(Object windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        public String getAtmoOpacity() {
            return atmoOpacity;
        }

        public void setAtmoOpacity(String atmoOpacity) {
            this.atmoOpacity = atmoOpacity;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }
    }

}
