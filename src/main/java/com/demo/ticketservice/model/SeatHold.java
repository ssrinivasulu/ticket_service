/**
 * 
 */
package com.demo.ticketservice.model;

import java.util.ArrayList;
import java.util.List;

import com.demo.ticketservice.domain.EventReservation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author ssrinivasulu
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,isGetterVisibility = JsonAutoDetect.Visibility.ANY,getterVisibility = JsonAutoDetect.Visibility.ANY)
public class SeatHold {
	public SeatHold(List<EventReservation> eventReservations) {
		this.eventReservations = eventReservations;
	}

	private List<EventReservation> eventReservations = new ArrayList<EventReservation>();

	public List<EventReservation> getEventReservations() {
		return eventReservations;
	}

	public void setEventReservations(List<EventReservation> eventReservations) {
		this.eventReservations = eventReservations;
	}

	
	
}
