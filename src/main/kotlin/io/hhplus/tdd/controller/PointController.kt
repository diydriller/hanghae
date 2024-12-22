package io.hhplus.tdd.controller

import io.hhplus.tdd.controller.dto.ChargePointRequest
import io.hhplus.tdd.controller.dto.UsePointRequest
import io.hhplus.tdd.facade.PointFacade
import io.hhplus.tdd.point.PointHistory
import io.hhplus.tdd.point.UserPoint
import io.hhplus.tdd.service.PointHistoryManagementService
import io.hhplus.tdd.service.PointManagementService
import io.hhplus.tdd.service.dto.ChargePointDto
import io.hhplus.tdd.service.dto.UsePointDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController(
    private val pointManagementService: PointManagementService,
    private val pointHistoryManagementService: PointHistoryManagementService,
    private val pointFacade: PointFacade
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    fun point(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
    ): UserPoint {
        return pointManagementService.getUserPoint(id)
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    fun history(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
    ): List<PointHistory> {
        return pointHistoryManagementService.getUserPointHistory(id)
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
        @RequestBody @Valid request: ChargePointRequest,
    ): UserPoint {
        val chargePointDto = ChargePointDto(id, request.amount)
        return pointFacade.chargeUserPoint(chargePointDto)
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    fun use(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
        @RequestBody @Valid request: UsePointRequest,
    ): UserPoint {
        val usePointDto = UsePointDto(id, request.amount)
        return pointFacade.useUserPoint(usePointDto)
    }
}