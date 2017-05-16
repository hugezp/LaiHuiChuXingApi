package com.lhcx.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class DriverInfo {
    private Integer id;

    private String companyid;

    private Integer address;
    
    private String addressname;

    private String drivername;

    private String drivergender;

    private Date driverbirthday;

    private String drivernationality;

    private String drivernation;

    private String drivermaritalstatus;

    private String driverlanguagelevel;

    private String driveeducation;

    private String drivercensus;

    private String driveraddress;

    private String drivercontactaddress;

    private String photo;

    private String licenseid;

    private String licensephoto;

    private String drivertype;

    private String driverphone;

    private Date getdriverlicensedate;

    private Date driverlicenseon;

    private Date driverlicenseoff;

    private Integer taxidriver;

    private String certificateno;

    private String networkcarissusorganization;

    private Date networkcarissusdate;

    private Date getnetworkproofdate;

    private Date networkcarproofon;

    private Date networkcarproofoff;

    private Date registerdate;

    private Integer fulltimedriver;

    private Integer commercialtype;

    private Integer indriverblacklist;

    private String contractcompany;

    private Date contracton;

    private Date contractoff;

    private String emergencycontact;

    private String emergencycontactphone;

    private String emergencycontactaddress;

    private Integer state;

    private Integer flag;

    private Date createtime;

    private Date updatetime;

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

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }
    
    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String addressname) {
        this.addressname = addressname == null ? null : addressname.trim();
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername == null ? null : drivername.trim();
    }

    public String getDrivergender() {
        return drivergender;
    }

    public void setDrivergender(String drivergender) {
        this.drivergender = drivergender == null ? null : drivergender.trim();
    }

    public Date getDriverbirthday() {
        return driverbirthday;
    }

    public void setDriverbirthday(Date driverbirthday) {
        this.driverbirthday = driverbirthday;
    }

    public String getDrivernationality() {
        return drivernationality;
    }

    public void setDrivernationality(String drivernationality) {
        this.drivernationality = drivernationality == null ? null : drivernationality.trim();
    }

    public String getDrivernation() {
        return drivernation;
    }

    public void setDrivernation(String drivernation) {
        this.drivernation = drivernation == null ? null : drivernation.trim();
    }

    public String getDrivermaritalstatus() {
        return drivermaritalstatus;
    }

    public void setDrivermaritalstatus(String drivermaritalstatus) {
        this.drivermaritalstatus = drivermaritalstatus == null ? null : drivermaritalstatus.trim();
    }

    public String getDriverlanguagelevel() {
        return driverlanguagelevel;
    }

    public void setDriverlanguagelevel(String driverlanguagelevel) {
        this.driverlanguagelevel = driverlanguagelevel == null ? null : driverlanguagelevel.trim();
    }

    public String getDriveeducation() {
        return driveeducation;
    }

    public void setDriveeducation(String driveeducation) {
        this.driveeducation = driveeducation == null ? null : driveeducation.trim();
    }

    public String getDrivercensus() {
        return drivercensus;
    }

    public void setDrivercensus(String drivercensus) {
        this.drivercensus = drivercensus == null ? null : drivercensus.trim();
    }

    public String getDriveraddress() {
        return driveraddress;
    }

    public void setDriveraddress(String driveraddress) {
        this.driveraddress = driveraddress == null ? null : driveraddress.trim();
    }

    public String getDrivercontactaddress() {
        return drivercontactaddress;
    }

    public void setDrivercontactaddress(String drivercontactaddress) {
        this.drivercontactaddress = drivercontactaddress == null ? null : drivercontactaddress.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public String getLicenseid() {
        return licenseid;
    }

    public void setLicenseid(String licenseid) {
        this.licenseid = licenseid == null ? null : licenseid.trim();
    }

    public String getLicensephoto() {
        return licensephoto;
    }

    public void setLicensephoto(String licensephoto) {
        this.licensephoto = licensephoto == null ? null : licensephoto.trim();
    }

    public String getDrivertype() {
        return drivertype;
    }

    public void setDrivertype(String drivertype) {
        this.drivertype = drivertype == null ? null : drivertype.trim();
    }

    public String getDriverphone() {
        return driverphone;
    }

    public void setDriverphone(String driverphone) {
        this.driverphone = driverphone == null ? null : driverphone.trim();
    }

    public Date getGetdriverlicensedate() {
        return getdriverlicensedate;
    }

    public void setGetdriverlicensedate(Date getdriverlicensedate) {
        this.getdriverlicensedate = getdriverlicensedate;
    }

    public Date getDriverlicenseon() {
        return driverlicenseon;
    }

    public void setDriverlicenseon(Date driverlicenseon) {
        this.driverlicenseon = driverlicenseon;
    }

    public Date getDriverlicenseoff() {
        return driverlicenseoff;
    }

    public void setDriverlicenseoff(Date driverlicenseoff) {
        this.driverlicenseoff = driverlicenseoff;
    }

    public Integer getTaxidriver() {
        return taxidriver;
    }

    public void setTaxidriver(Integer taxidriver) {
        this.taxidriver = taxidriver;
    }

    public String getCertificateno() {
        return certificateno;
    }

    public void setCertificateno(String certificateno) {
        this.certificateno = certificateno == null ? null : certificateno.trim();
    }

    public String getNetworkcarissusorganization() {
        return networkcarissusorganization;
    }

    public void setNetworkcarissusorganization(String networkcarissusorganization) {
        this.networkcarissusorganization = networkcarissusorganization == null ? null : networkcarissusorganization.trim();
    }

    public Date getNetworkcarissusdate() {
        return networkcarissusdate;
    }

    public void setNetworkcarissusdate(Date networkcarissusdate) {
        this.networkcarissusdate = networkcarissusdate;
    }

    public Date getGetnetworkproofdate() {
        return getnetworkproofdate;
    }

    public void setGetnetworkproofdate(Date getnetworkproofdate) {
        this.getnetworkproofdate = getnetworkproofdate;
    }

    public Date getNetworkcarproofon() {
        return networkcarproofon;
    }

    public void setNetworkcarproofon(Date networkcarproofon) {
        this.networkcarproofon = networkcarproofon;
    }

    public Date getNetworkcarproofoff() {
        return networkcarproofoff;
    }

    public void setNetworkcarproofoff(Date networkcarproofoff) {
        this.networkcarproofoff = networkcarproofoff;
    }

    public Date getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Date registerdate) {
        this.registerdate = registerdate;
    }

    public Integer getFulltimedriver() {
        return fulltimedriver;
    }

    public void setFulltimedriver(Integer fulltimedriver) {
        this.fulltimedriver = fulltimedriver;
    }

    public Integer getCommercialtype() {
        return commercialtype;
    }

    public void setCommercialtype(Integer commercialtype) {
        this.commercialtype = commercialtype;
    }

    public Integer getIndriverblacklist() {
        return indriverblacklist;
    }

    public void setIndriverblacklist(Integer indriverblacklist) {
        this.indriverblacklist = indriverblacklist;
    }

    public String getContractcompany() {
        return contractcompany;
    }

    public void setContractcompany(String contractcompany) {
        this.contractcompany = contractcompany == null ? null : contractcompany.trim();
    }

    public Date getContracton() {
        return contracton;
    }

    public void setContracton(Date contracton) {
        this.contracton = contracton;
    }

    public Date getContractoff() {
        return contractoff;
    }

    public void setContractoff(Date contractoff) {
        this.contractoff = contractoff;
    }

    public String getEmergencycontact() {
        return emergencycontact;
    }

    public void setEmergencycontact(String emergencycontact) {
        this.emergencycontact = emergencycontact == null ? null : emergencycontact.trim();
    }

    public String getEmergencycontactphone() {
        return emergencycontactphone;
    }

    public void setEmergencycontactphone(String emergencycontactphone) {
        this.emergencycontactphone = emergencycontactphone == null ? null : emergencycontactphone.trim();
    }

    public String getEmergencycontactaddress() {
        return emergencycontactaddress;
    }

    public void setEmergencycontactaddress(String emergencycontactaddress) {
        this.emergencycontactaddress = emergencycontactaddress == null ? null : emergencycontactaddress.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    
    public DriverInfo() {
		
	}
    
    public DriverInfo(JSONObject jsonRequest) {
		// TODO Auto-generated constructor stub
    	if (jsonRequest != null) {
    		//基本信息
			this.driverphone = jsonRequest.getString("phone");
			this.photo = jsonRequest.getString("photo");
			this.addressname = jsonRequest.getString("addressName");
			this.drivername = jsonRequest.getString("driverName");
			this.licenseid = jsonRequest.getString("licenseId");
			this.drivernation = jsonRequest.getString("driverNation");
			this.drivernationality = jsonRequest.getString("driverNationality");
			this.address = jsonRequest.getInteger("address");
			
			//证件信息
			this.licensephoto = jsonRequest.getString("licensePhoto");
			this.getdriverlicensedate = jsonRequest.getDate("getDriverLicenseDate");
			this.driverlicenseon = jsonRequest.getDate("driverLicenseOn");
			this.driverlicenseoff = jsonRequest.getDate("driverLicenseOff");
			this.fulltimedriver = jsonRequest.getInteger("fullTimeDriver");
			
		}
	}
}