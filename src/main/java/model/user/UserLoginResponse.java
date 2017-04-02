package model.user;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserLoginResponse {
	private boolean result;
	private String sid;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

}
