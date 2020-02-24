package com.parking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.exception.CarAlreadyParkedException;
import com.parking.model.*;
import com.parking.service.ParkingManager;
import com.parking.service.TicketingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ParkingController.class)
public class ParkingControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ParkingManager parkingManager;

    @MockBean
    private TicketingService ticketingService;

    @Before
    public void init() throws CarAlreadyParkedException {
        PricingPolicy pricingPolicy = new PricingPolicy(1.5, 1.5);
        Slot slot = new Slot(CarType.ELECTRIC_LOW_POWER, false);
        slot.setId(1L);
        Ticket ticket = new Ticket("AAAA", slot, LocalDateTime.of(2020, 2, 15, 10, 30));
        ticket.setId(1L);
        Car car = new Car("AAAA", CarType.ELECTRIC_LOW_POWER);
        ticket.setEndTime(LocalDateTime.of(2020, 2, 15, 12, 29));
        ticket.setPrice(4.5);
        doReturn(true).when(parkingManager).updatePricingPolicy(any());
        doReturn(Optional.of(slot)).when(parkingManager).enterParking(car);
        doReturn(Optional.of(ticket)).when(parkingManager).exitParking("AAAA");
    }

    @Test
    public void testEnterParking() throws Exception {
        mvc.perform(post("/car")
                .content(toJson(new Car("AAAA", CarType.ELECTRIC_LOW_POWER)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.carType", is("ELECTRIC_LOW_POWER")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isFree", is(false)));
    }

    @Test
    public void testExitParking() throws Exception {
        mvc.perform(delete("/car/AAAA")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberPlate", is("AAAA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(4.5)));
    }

    @Test
    public void testUpdatePricingPolicy() throws Exception {
        PricingPolicy pricingPolicy = new PricingPolicy(2.5, 1.5);

        mvc.perform(put("/pricingPolicy")
                .content(toJson(pricingPolicy))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fixedAmount", is(2.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hourPrice", is(1.5)));
    }

    @Test
    public void testEnterSameCar() throws Exception {
        mvc.perform(post("/car")
                .content(toJson(new Car("AAAA", CarType.ELECTRIC_LOW_POWER)))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(post("/car")
                .content(toJson(new Car("AAAA", CarType.ELECTRIC_LOW_POWER))));    }

    private byte[] toJson(Object object) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        return mapper.writeValueAsBytes(object);
        return new ObjectMapper().writeValueAsBytes((object));
    }

}
