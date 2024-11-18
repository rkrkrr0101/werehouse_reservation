package rkrk.reservation.helper

import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringTestContainerTest
@Transactional
annotation class SpringTestContainerTestWithTransactional
