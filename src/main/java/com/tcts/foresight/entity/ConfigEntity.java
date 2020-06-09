package com.tcts.foresight.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "config")
public class ConfigEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "config_seq_generator")
	@SequenceGenerator(name = "config_seq_generator", sequenceName = "config_seq", allocationSize = 1)
	@Column(name = "config_id")
	private Long configId;

	@Column(name = "config_value")
	private String configValue;

	@Column(name = "config_key")
	private String configKey;

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	@Override
	public String toString() {
		return "ConfigEntity [configId=" + configId + ", configValue=" + configValue + ", configKey=" + configKey + "]";
	}

	public ConfigEntity(Long configId, String configValue, String configKey) {
		super();
		this.configId = configId;
		this.configValue = configValue;
		this.configKey = configKey;
	}

	public ConfigEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}
