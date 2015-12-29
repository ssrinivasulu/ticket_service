/**
 * 
 */
package com.demo.ticketservice.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ssrinivasulu
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
@Entity (name = "EventVenue")
@Table (name = "event_venue")
//@SequenceGenerator(name = "event_venue_seq", sequenceName = "event_venue_seq")
public class EventVenue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue //(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column (name = "name", nullable = false)
	private String name;
	@Column (name = "description", nullable = false)
	private String description;
	
	@OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "eventVenue")
   	private Set<EventVenueTicketLevel> eventVenueTicketLevels;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<EventVenueTicketLevel> getEventVenueTicketLevels() {
		return eventVenueTicketLevels;
	}
	public void setEventVenueTicketLevels(Set<EventVenueTicketLevel> eventVenueTicketLevels) {
		this.eventVenueTicketLevels = eventVenueTicketLevels;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder(55, 113).append(this.id).append(this.name).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		EventVenue other = (EventVenue) o;
		return new EqualsBuilder()
				         .append(other.name, this.name)
				         .append(other.id, this.id)
				         .isEquals();
	}
	
}
