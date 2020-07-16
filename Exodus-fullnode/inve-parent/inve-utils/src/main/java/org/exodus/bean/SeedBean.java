package org.exodus.bean;

public class SeedBean {
      private String host;
      private Integer port;
      private String pubkey;
      private String domainName;
      
      public SeedBean () {
		
	}

	public SeedBean(String host, Integer port, String pubkey, String domainName) {
		super();
		this.host = host;
		this.port = port;
		this.pubkey = pubkey;
		this.domainName = domainName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Override
	public String toString() {
		return "SeedBean [host=" + host + ", port=" + port + ", pubkey=" + pubkey + ", domainName=" + domainName + "]";
	}

      
}
