package pl.draciel.octocat.concurrent

import javax.inject.Qualifier
import kotlin.annotation.AnnotationTarget.*

@Qualifier
@Retention(AnnotationRetention.BINARY)
@Target(FIELD, VALUE_PARAMETER, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class ComputationScheduler
