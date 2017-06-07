package com.lhcx.model;

import java.util.Date;

/**
 * 投诉与建议实体类
 * @author YangGuang
 *
 */
public class Suggests {
    private Integer id;

    private String identitytoken;

    private String contactinformation;

    private String suggest;

    private String photo;

    private Integer source;

    private Integer isdel;

    private Date createTime;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentitytoken() {
        return identitytoken;
    }

    public void setIdentitytoken(String identitytoken) {
        this.identitytoken = identitytoken == null ? null : identitytoken.trim();
    }

    public String getContactinformation() {
        return contactinformation;
    }

    public void setContactinformation(String contactinformation) {
        this.contactinformation = contactinformation == null ? null : contactinformation.trim();
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest == null ? null : suggest.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}