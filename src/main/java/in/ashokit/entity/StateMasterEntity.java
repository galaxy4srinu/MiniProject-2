package in.ashokit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "STATES_MASTER")
public class StateMasterEntity {

	@Id
	@GeneratedValue
	@Column(name = "STATE_ID")
	private int stateId;

	@Column(name = "STATE_NAME")
	private String stateName;

	@Column(name = "COUNTRY_ID")
	private int countryId;

}
