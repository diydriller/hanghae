package io.hhplus.tdd.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.controller.dto.ChargePointRequest
import io.hhplus.tdd.controller.dto.UsePointRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class PointControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @DisplayName("API call should increase the balance by specified amount")
    @Test
    fun chargePointSuccessTest() {
        // given
        val userId = 1L
        val request = ChargePointRequest(1000L)
        val requestJson = objectMapper.writeValueAsString(request)

        // when & then
        val initialResponse = mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andReturn()
        val initialPoint = objectMapper.readTree(initialResponse.response.contentAsString)["point"].asLong()

        mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.point").value(initialPoint + request.amount))
    }

    @DisplayName("API call should decrease the balance by specified amount")
    @Test
    fun usePointSuccessTest() {
        // given
        val userId = 2L
        val chargePointRequest = ChargePointRequest(5000L)
        val chargePointRequestJson = objectMapper.writeValueAsString(chargePointRequest)
        val usePointRequest = UsePointRequest(2000L)
        val usePointRequestJson = objectMapper.writeValueAsString(usePointRequest)

        // when & then
        val initialResponse = mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointRequestJson)
        )
            .andExpect(status().isOk)
            .andReturn()
        val initialPoint = objectMapper.readTree(initialResponse.response.contentAsString)["point"].asLong()

        mockMvc.perform(
            patch("/point/$userId/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usePointRequestJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.point").value(initialPoint - usePointRequest.amount))
    }

    @DisplayName("API call respond 500 error when using point exceeding balance")
    @Test
    fun usePointFailTest() {
        // given
        val userId = 3L
        val chargePointRequest = ChargePointRequest(2000L)
        val chargePointRequestJson = objectMapper.writeValueAsString(chargePointRequest)
        val usePointRequest = UsePointRequest(10000L)
        val usePointRequestJson = objectMapper.writeValueAsString(usePointRequest)

        // when & then
        mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointRequestJson)
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            patch("/point/$userId/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usePointRequestJson)
        )
            .andExpect(status().isInternalServerError)
    }
}