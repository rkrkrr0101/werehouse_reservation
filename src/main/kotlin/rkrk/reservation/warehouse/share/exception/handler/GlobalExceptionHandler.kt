package rkrk.reservation.warehouse.share.exception.handler

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import rkrk.reservation.warehouse.share.Result
import rkrk.reservation.warehouse.share.exception.exception.IllegalDomainException
import rkrk.reservation.warehouse.share.exception.exception.NotFoundEntityException
import rkrk.reservation.warehouse.share.exception.exception.OverlapException

@ControllerAdvice
@RestController
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(IllegalDomainException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalDomainException(e: IllegalDomainException): Result<String> {
        log.warn(e.message)
        return Result(e.msg)
    }

    @ExceptionHandler(NotFoundEntityException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNotFoundEntityException(e: NotFoundEntityException): Result<String> {
        log.warn(e.message)
        return Result(e.msg)
    }

    @ExceptionHandler(OverlapException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOverlapException(e: OverlapException): Result<String> {
        log.warn(e.message)
        return Result(e.msg)
    }
}
