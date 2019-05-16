package pl.draciel.octocat.core.utility

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import retrofit2.HttpException
import retrofit2.Response

object ResponseTransformers {

    @JvmStatic
    fun <T> httpStatus(statusCode: Int): SingleTransformer<Response<T>, Response<T>> {
        return SingleTransformer {
            it.flatMap { response ->
                if (!response.isSuccessful && response.code() == statusCode) {
                    return@flatMap Single.error<Response<T>>(HttpException(response))
                }
                return@flatMap Single.just(response)
            }
        }
    }

    @JvmStatic
    fun <T> httpStatusObservable(statusCode: Int): ObservableTransformer<Response<T>, Response<T>> {
        return ObservableTransformer {
            it.flatMap { response ->
                if (!response.isSuccessful && response.code() == statusCode) {
                    return@flatMap Observable.error<Response<T>>(HttpException(response))
                }
                return@flatMap Observable.just(response)
            }
        }
    }
}
