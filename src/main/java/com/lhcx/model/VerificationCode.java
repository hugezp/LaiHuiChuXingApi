package com.lhcx.model;

import java.util.Date;

public class VerificationCode {
    private Integer id;

    private String phone;

    private String code;

    private String usertype;

    private Date createtime;

    private Date verificationtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype == null ? null : usertype.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getVerificationtime() {
        return verificationtime;
    }

    public void setVerificationtime(Date verificationtime) {
        this.verificationtime = verificationtime;
    }
}