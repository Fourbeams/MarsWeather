package com.fourbeams.marsweather.domain;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POJO {
    @SerializedName("descriptions")
    @Expose
    private Descriptions descriptions;
    @SerializedName("soles")
    @Expose
    private List<Sole> soles = new ArrayList<Sole>();
    /**
     * @return The descriptions
     */
    public Descriptions getDescriptions() {
        return descriptions;
    }
    /**
     * @param descriptions The descriptions
     */
    public void setDescriptions(Descriptions descriptions) {
        this.descriptions = descriptions;
    }
    /**
     * @return The soles
     */
    public List<Sole> getSoles() {
        return soles;
    }
    /**
     * @param soles The soles
     */
    public void setSoles(List<Sole> soles) {
        this.soles = soles;
    }

    public class Sole {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("terrestrial_date")
        @Expose
        private String terrestrialDate;
        @SerializedName("sol")
        @Expose
        private String sol;
        @SerializedName("ls")
        @Expose
        private String ls;
        @SerializedName("season")
        @Expose
        private String season;
        @SerializedName("min_temp")
        @Expose
        private String minTemp;
        @SerializedName("max_temp")
        @Expose
        private String maxTemp;
        @SerializedName("pressure")
        @Expose
        private String pressure;
        @SerializedName("pressure_string")
        @Expose
        private String pressureString;
        @SerializedName("abs_humidity")
        @Expose
        private String absHumidity;
        @SerializedName("wind_speed")
        @Expose
        private String windSpeed;
        @SerializedName("wind_direction")
        @Expose
        private String windDirection;
        @SerializedName("atmo_opacity")
        @Expose
        private String atmoOpacity;
        @SerializedName("sunrise")
        @Expose
        private String sunrise;
        @SerializedName("sunset")
        @Expose
        private String sunset;
        @SerializedName("local_uv_irradiance_index")
        @Expose
        private String localUvIrradianceIndex;
        @SerializedName("min_gts_temp")
        @Expose
        private String minGtsTemp;
        @SerializedName("max_gts_temp")
        @Expose
        private String maxGtsTemp;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The terrestrialDate
         */
        public String getTerrestrialDate() {
            return terrestrialDate;
        }

        /**
         * @param terrestrialDate The terrestrial_date
         */
        public void setTerrestrialDate(String terrestrialDate) {
            this.terrestrialDate = terrestrialDate;
        }

        /**
         * @return The sol
         */
        public String getSol() {
            return sol;
        }

        /**
         * @param sol The sol
         */
        public void setSol(String sol) {
            this.sol = sol;
        }

        /**
         * @return The ls
         */
        public String getLs() {
            return ls;
        }

        /**
         * @param ls The ls
         */
        public void setLs(String ls) {
            this.ls = ls;
        }

        /**
         * @return The season
         */
        public String getSeason() {
            return season;
        }

        /**
         * @param season The season
         */
        public void setSeason(String season) {
            this.season = season;
        }

        /**
         * @return The minTemp
         */
        public String getMinTemp() {
            return minTemp;
        }

        /**
         * @param minTemp The min_temp
         */
        public void setMinTemp(String minTemp) {
            this.minTemp = minTemp;
        }

        /**
         * @return The maxTemp
         */
        public String getMaxTemp() {
            return maxTemp;
        }

        /**
         * @param maxTemp The max_temp
         */
        public void setMaxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
        }

        /**
         * @return The pressure
         */
        public String getPressure() {
            return pressure;
        }

        /**
         * @param pressure The pressure
         */
        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        /**
         * @return The pressureString
         */
        public String getPressureString() {
            return pressureString;
        }

        /**
         * @param pressureString The pressure_string
         */
        public void setPressureString(String pressureString) {
            this.pressureString = pressureString;
        }

        /**
         * @return The absHumidity
         */
        public String getAbsHumidity() {
            return absHumidity;
        }

        /**
         * @param absHumidity The abs_humidity
         */
        public void setAbsHumidity(String absHumidity) {
            this.absHumidity = absHumidity;
        }

        /**
         * @return The windSpeed
         */
        public String getWindSpeed() {
            return windSpeed;
        }

        /**
         * @param windSpeed The wind_speed
         */
        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        /**
         * @return The windDirection
         */
        public String getWindDirection() {
            return windDirection;
        }

        /**
         * @param windDirection The wind_direction
         */
        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        /**
         * @return The atmoOpacity
         */
        public String getAtmoOpacity() {
            return atmoOpacity;
        }

        /**
         * @param atmoOpacity The atmo_opacity
         */
        public void setAtmoOpacity(String atmoOpacity) {
            this.atmoOpacity = atmoOpacity;
        }

        /**
         * @return The sunrise
         */
        public String getSunrise() {
            return sunrise;
        }

        /**
         * @param sunrise The sunrise
         */
        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        /**
         * @return The sunset
         */
        public String getSunset() {
            return sunset;
        }

        /**
         * @param sunset The sunset
         */
        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        /**
         * @return The localUvIrradianceIndex
         */
        public String getLocalUvIrradianceIndex() {
            return localUvIrradianceIndex;
        }

        /**
         * @param localUvIrradianceIndex The local_uv_irradiance_index
         */
        public void setLocalUvIrradianceIndex(String localUvIrradianceIndex) {
            this.localUvIrradianceIndex = localUvIrradianceIndex;
        }

        /**
         * @return The minGtsTemp
         */
        public String getMinGtsTemp() {
            return minGtsTemp;
        }

        /**
         * @param minGtsTemp The min_gts_temp
         */
        public void setMinGtsTemp(String minGtsTemp) {
            this.minGtsTemp = minGtsTemp;
        }

        /**
         * @return The maxGtsTemp
         */
        public String getMaxGtsTemp() {
            return maxGtsTemp;
        }

        /**
         * @param maxGtsTemp The max_gts_temp
         */
        public void setMaxGtsTemp(String maxGtsTemp) {
            this.maxGtsTemp = maxGtsTemp;
        }


    }

    public class Descriptions {

        @SerializedName("disclaimer_en")
        @Expose
        private String disclaimerEn;
        @SerializedName("disclaimer_es")
        @Expose
        private String disclaimerEs;
        @SerializedName("terrestrial_date_desc_en")
        @Expose
        private String terrestrialDateDescEn;
        @SerializedName("terrestrial_date_desc_es")
        @Expose
        private String terrestrialDateDescEs;
        @SerializedName("temp_desc_en")
        @Expose
        private String tempDescEn;
        @SerializedName("temp_desc_es")
        @Expose
        private String tempDescEs;
        @SerializedName("pressure_desc_en")
        @Expose
        private String pressureDescEn;
        @SerializedName("pressure_desc_es")
        @Expose
        private String pressureDescEs;
        @SerializedName("abs_humidity_desc_en")
        @Expose
        private String absHumidityDescEn;
        @SerializedName("abs_humidity_desc_es")
        @Expose
        private String absHumidityDescEs;
        @SerializedName("wind_desc_en")
        @Expose
        private String windDescEn;
        @SerializedName("wind_desc_es")
        @Expose
        private String windDescEs;
        @SerializedName("gts_temp_desc_en")
        @Expose
        private String gtsTempDescEn;
        @SerializedName("gts_temp_desc_es")
        @Expose
        private String gtsTempDescEs;
        @SerializedName("local_uv_irradiance_index_desc_en")
        @Expose
        private String localUvIrradianceIndexDescEn;
        @SerializedName("local_uv_irradiance_index_desc_es")
        @Expose
        private String localUvIrradianceIndexDescEs;
        @SerializedName("atmo_opacity_desc_en")
        @Expose
        private String atmoOpacityDescEn;
        @SerializedName("atmo_opacity_desc_es")
        @Expose
        private String atmoOpacityDescEs;
        @SerializedName("ls_desc_en")
        @Expose
        private String lsDescEn;
        @SerializedName("ls_desc_es")
        @Expose
        private String lsDescEs;
        @SerializedName("season_desc_en")
        @Expose
        private String seasonDescEn;
        @SerializedName("season_desc_es")
        @Expose
        private String seasonDescEs;
        @SerializedName("sunrise_sunset_desc_en")
        @Expose
        private String sunriseSunsetDescEn;
        @SerializedName("sunrise_sunset_desc_es")
        @Expose
        private String sunriseSunsetDescEs;

        /**
         * @return The disclaimerEn
         */
        public String getDisclaimerEn() {
            return disclaimerEn;
        }

        /**
         * @param disclaimerEn The disclaimer_en
         */
        public void setDisclaimerEn(String disclaimerEn) {
            this.disclaimerEn = disclaimerEn;
        }

        /**
         * @return The disclaimerEs
         */
        public String getDisclaimerEs() {
            return disclaimerEs;
        }

        /**
         * @param disclaimerEs The disclaimer_es
         */
        public void setDisclaimerEs(String disclaimerEs) {
            this.disclaimerEs = disclaimerEs;
        }

        /**
         * @return The terrestrialDateDescEn
         */
        public String getTerrestrialDateDescEn() {
            return terrestrialDateDescEn;
        }

        /**
         * @param terrestrialDateDescEn The terrestrial_date_desc_en
         */
        public void setTerrestrialDateDescEn(String terrestrialDateDescEn) {
            this.terrestrialDateDescEn = terrestrialDateDescEn;
        }

        /**
         * @return The terrestrialDateDescEs
         */
        public String getTerrestrialDateDescEs() {
            return terrestrialDateDescEs;
        }

        /**
         * @param terrestrialDateDescEs The terrestrial_date_desc_es
         */
        public void setTerrestrialDateDescEs(String terrestrialDateDescEs) {
            this.terrestrialDateDescEs = terrestrialDateDescEs;
        }

        /**
         * @return The tempDescEn
         */
        public String getTempDescEn() {
            return tempDescEn;
        }

        /**
         * @param tempDescEn The temp_desc_en
         */
        public void setTempDescEn(String tempDescEn) {
            this.tempDescEn = tempDescEn;
        }

        /**
         * @return The tempDescEs
         */
        public String getTempDescEs() {
            return tempDescEs;
        }

        /**
         * @param tempDescEs The temp_desc_es
         */
        public void setTempDescEs(String tempDescEs) {
            this.tempDescEs = tempDescEs;
        }

        /**
         * @return The pressureDescEn
         */
        public String getPressureDescEn() {
            return pressureDescEn;
        }

        /**
         * @param pressureDescEn The pressure_desc_en
         */
        public void setPressureDescEn(String pressureDescEn) {
            this.pressureDescEn = pressureDescEn;
        }

        /**
         * @return The pressureDescEs
         */
        public String getPressureDescEs() {
            return pressureDescEs;
        }

        /**
         * @param pressureDescEs The pressure_desc_es
         */
        public void setPressureDescEs(String pressureDescEs) {
            this.pressureDescEs = pressureDescEs;
        }

        /**
         * @return The absHumidityDescEn
         */
        public String getAbsHumidityDescEn() {
            return absHumidityDescEn;
        }

        /**
         * @param absHumidityDescEn The abs_humidity_desc_en
         */
        public void setAbsHumidityDescEn(String absHumidityDescEn) {
            this.absHumidityDescEn = absHumidityDescEn;
        }

        /**
         * @return The absHumidityDescEs
         */
        public String getAbsHumidityDescEs() {
            return absHumidityDescEs;
        }

        /**
         * @param absHumidityDescEs The abs_humidity_desc_es
         */
        public void setAbsHumidityDescEs(String absHumidityDescEs) {
            this.absHumidityDescEs = absHumidityDescEs;
        }

        /**
         * @return The windDescEn
         */
        public String getWindDescEn() {
            return windDescEn;
        }

        /**
         * @param windDescEn The wind_desc_en
         */
        public void setWindDescEn(String windDescEn) {
            this.windDescEn = windDescEn;
        }

        /**
         * @return The windDescEs
         */
        public String getWindDescEs() {
            return windDescEs;
        }

        /**
         * @param windDescEs The wind_desc_es
         */
        public void setWindDescEs(String windDescEs) {
            this.windDescEs = windDescEs;
        }

        /**
         * @return The gtsTempDescEn
         */
        public String getGtsTempDescEn() {
            return gtsTempDescEn;
        }

        /**
         * @param gtsTempDescEn The gts_temp_desc_en
         */
        public void setGtsTempDescEn(String gtsTempDescEn) {
            this.gtsTempDescEn = gtsTempDescEn;
        }

        /**
         * @return The gtsTempDescEs
         */
        public String getGtsTempDescEs() {
            return gtsTempDescEs;
        }

        /**
         * @param gtsTempDescEs The gts_temp_desc_es
         */
        public void setGtsTempDescEs(String gtsTempDescEs) {
            this.gtsTempDescEs = gtsTempDescEs;
        }

        /**
         * @return The localUvIrradianceIndexDescEn
         */
        public String getLocalUvIrradianceIndexDescEn() {
            return localUvIrradianceIndexDescEn;
        }

        /**
         * @param localUvIrradianceIndexDescEn The local_uv_irradiance_index_desc_en
         */
        public void setLocalUvIrradianceIndexDescEn(String localUvIrradianceIndexDescEn) {
            this.localUvIrradianceIndexDescEn = localUvIrradianceIndexDescEn;
        }

        /**
         * @return The localUvIrradianceIndexDescEs
         */
        public String getLocalUvIrradianceIndexDescEs() {
            return localUvIrradianceIndexDescEs;
        }

        /**
         * @param localUvIrradianceIndexDescEs The local_uv_irradiance_index_desc_es
         */
        public void setLocalUvIrradianceIndexDescEs(String localUvIrradianceIndexDescEs) {
            this.localUvIrradianceIndexDescEs = localUvIrradianceIndexDescEs;
        }

        /**
         * @return The atmoOpacityDescEn
         */
        public String getAtmoOpacityDescEn() {
            return atmoOpacityDescEn;
        }

        /**
         * @param atmoOpacityDescEn The atmo_opacity_desc_en
         */
        public void setAtmoOpacityDescEn(String atmoOpacityDescEn) {
            this.atmoOpacityDescEn = atmoOpacityDescEn;
        }

        /**
         * @return The atmoOpacityDescEs
         */
        public String getAtmoOpacityDescEs() {
            return atmoOpacityDescEs;
        }

        /**
         * @param atmoOpacityDescEs The atmo_opacity_desc_es
         */
        public void setAtmoOpacityDescEs(String atmoOpacityDescEs) {
            this.atmoOpacityDescEs = atmoOpacityDescEs;
        }

        /**
         * @return The lsDescEn
         */
        public String getLsDescEn() {
            return lsDescEn;
        }

        /**
         * @param lsDescEn The ls_desc_en
         */
        public void setLsDescEn(String lsDescEn) {
            this.lsDescEn = lsDescEn;
        }

        /**
         * @return The lsDescEs
         */
        public String getLsDescEs() {
            return lsDescEs;
        }

        /**
         * @param lsDescEs The ls_desc_es
         */
        public void setLsDescEs(String lsDescEs) {
            this.lsDescEs = lsDescEs;
        }

        /**
         * @return The seasonDescEn
         */
        public String getSeasonDescEn() {
            return seasonDescEn;
        }

        /**
         * @param seasonDescEn The season_desc_en
         */
        public void setSeasonDescEn(String seasonDescEn) {
            this.seasonDescEn = seasonDescEn;
        }

        /**
         * @return The seasonDescEs
         */
        public String getSeasonDescEs() {
            return seasonDescEs;
        }

        /**
         * @param seasonDescEs The season_desc_es
         */
        public void setSeasonDescEs(String seasonDescEs) {
            this.seasonDescEs = seasonDescEs;
        }

        /**
         * @return The sunriseSunsetDescEn
         */
        public String getSunriseSunsetDescEn() {
            return sunriseSunsetDescEn;
        }

        /**
         * @param sunriseSunsetDescEn The sunrise_sunset_desc_en
         */
        public void setSunriseSunsetDescEn(String sunriseSunsetDescEn) {
            this.sunriseSunsetDescEn = sunriseSunsetDescEn;
        }

        /**
         * @return The sunriseSunsetDescEs
         */
        public String getSunriseSunsetDescEs() {
            return sunriseSunsetDescEs;
        }

        /**
         * @param sunriseSunsetDescEs The sunrise_sunset_desc_es
         */
        public void setSunriseSunsetDescEs(String sunriseSunsetDescEs) {
            this.sunriseSunsetDescEs = sunriseSunsetDescEs;
        }

    }
}