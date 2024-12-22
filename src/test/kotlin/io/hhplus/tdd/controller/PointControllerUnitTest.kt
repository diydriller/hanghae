package io.hhplus.tdd.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.controller.dto.ChargePointRequest
import io.hhplus.tdd.facade.PointFacade
import io.hhplus.tdd.service.PointHistoryManagementService
import io.hhplus.tdd.service.PointManagementService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PointController::class)
class PointControllerUnitTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var pointManagementService: PointManagementService

    @MockBean
    private lateinit var pointHistoryManagementService: PointHistoryManagementService

    @MockBean
    private lateinit var pointFacade: PointFacade

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("amount less than 1 should fail validation")
    fun amountValidationFailTest() {
        // given
        val userId = 1L

        val request = ChargePointRequest(-1000L)
        val jsonRequest = objectMapper.writeValueAsString(request)

        // when & then
        mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    @DisplayName("userId less than 1 should fail validation")
    fun userIdValidationFailTest() {
        // given
        val userId = -1L

        val request = ChargePointRequest(-1000L)
        val jsonRequest = objectMapper.writeValueAsString(request)

        // when & then
        mockMvc.perform(
            patch("/point/$userId/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        )
            .andExpect(status().isBadRequest())
    }
}