package jalilurrahman.com.beacon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by einfochips on 10/16/17.
 */

public class BeaconsInfo {

    @SerializedName("resourcetype")
    private String resourcetype;

    @SerializedName("resourcename")
    private String resourcename;

    @SerializedName("resourceid")
    private String resourceid;

    @SerializedName("beacondata")
    private List<BeaconData> beacondata;

    public String getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
    }

    public String getResourcename() {
        return resourcename;
    }

    public void setResourcename(String resourcename) {
        this.resourcename = resourcename;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public List<BeaconData> getResultset() {
        return beacondata;
    }

    public void setResultset(List<BeaconData> beacondata) {
        this.beacondata = beacondata;
    }

    public class BeaconData {

        @SerializedName("beacon_uuid")
        private String beacon_uuid;

        @SerializedName("distance")
        private String distance;

        @SerializedName("rssi")
        private String rssi;

        @SerializedName("tx_power")
        private String tx_power;

        @SerializedName("protocol")
        private String protocol;

        @SerializedName("vendor")
        private String vendor;

        @SerializedName("major")
        private String major;

        @SerializedName("minor")
        private String minor;

        @SerializedName("range")
        private String range;


        public BeaconData(String beacon_uuid, String distance, String rssi, String tx_power, String protocol, String vendor, String major, String minor, String range) {
            this.beacon_uuid = beacon_uuid;
            this.distance = distance;
            this.rssi = rssi;
            this.tx_power = tx_power;
            this.protocol = protocol;
            this.vendor = vendor;
            this.major = major;
            this.minor = minor;
            this.range = range;
        }

        public String getBeacon_uuid() {
            return beacon_uuid;
        }

        public void setBeacon_uuid(String beacon_uuid) {
            this.beacon_uuid = beacon_uuid;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getRssi() {
            return rssi;
        }

        public void setRssi(String rssi) {
            this.rssi = rssi;
        }

        public String getTx_power() {
            return tx_power;
        }

        public void setTx_power(String tx_power) {
            this.tx_power = tx_power;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getMinor() {
            return minor;
        }

        public void setMinor(String minor) {
            this.minor = minor;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }
    }
}
