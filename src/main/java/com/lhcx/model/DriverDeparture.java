package com.lhcx.model;

import java.util.Date;

public class DriverDeparture {
    private Integer id;

    private String companyid;

    private String routeid;

    private String licenseid;

    private String vehicleno;

    private String departure;

    private String deplongitude;

    private String deplatitude;

    private String destination;

    private String destlongittude;

    private String destlatitude;

    private Integer encrypt;

    private Date routecreatetime;

    private String routemile;

    private String routenote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid == null ? null : companyid.trim();
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid == null ? null : routeid.trim();
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid == null ? null : licenseid.trim();
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno == null ? null : vehicleno.trim();
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure == null ? null : departure.trim();
    }

    public String getDeplongitude() {
        return deplongitude;
    }

    public void setDeplongitude(String deplongitude) {
        this.deplongitude = deplongitude == null ? null : deplongitude.trim();
    }

    public String getDeplatitude() {
        return deplatitude;
    }

    public void setDeplatitude(String deplatitude) {
        this.deplatitude = deplatitude == null ? null : deplatitude.trim();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination == null ? null : destination.trim();
    }

    public String getDestlongittude() {
        return destlongittude;
    }

    public void setDestlongittude(String destlongittude) {
        this.destlongittude = destlongittude == null ? null : destlongittude.trim();
    }

    public String getDestlatitude() {
        return destlatitude;
    }

    public void setDestlatitude(String destlatitude) {
        this.destlatitude = destlatitude == null ? null : destlatitude.trim();
    }

    public Integer getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }

    public Date getRoutecreatetime() {
        return routecreatetime;
    }

    public void setRoutecreatetime(Date routecreatetime) {
        this.routecreatetime = routecreatetime;
    }

    public String getRoutemile() {
        return routemile;
    }

    public void setRoutemile(String routemile) {
        this.routemile = routemile == null ? null : routemile.trim();
    }

    public String getRoutenote() {
        return routenote;
    }

    public void setRoutenote(String routenote) {
        this.routenote = routenote == null ? null : routenote.trim();
    }
}