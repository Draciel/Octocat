package pl.draciel.octocat.core.utility

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

typealias IdProvider<T> = (T) -> Long
typealias IdChanger<T> = (T, Long) -> T

abstract class InMemoryCrudRepository<T>(
    protected val idProvider: IdProvider<T>,
    protected val idChanger: IdChanger<T>
) {

    protected val db: MutableMap<Long, T?> = ConcurrentHashMap()

    protected val idIncrementer = AtomicLong()

    private val saveListeners: MutableList<SaveListener<T>> = ArrayList()

    private val deleteListeners: MutableList<DeleteListener<T>> = ArrayList()

    // We are retrieving this same object which we saved in db, so it is safe
    @Suppress("UNCHECKED_CAST")
    fun <S : T> save(s: S): S {
        var id = idProvider(s)
        var objectToSave: T = s
        if (id <= 0) {
            id = this.idIncrementer.incrementAndGet()
            objectToSave = idChanger(s, id)
        }
        db[id] = objectToSave
        val savedObj = db[id] as S
        saveListeners.forEach { l -> l.onSave(savedObj) }
        return savedObj
    }

    fun <S : T> saveAll(iterable: Iterable<S>): Iterable<S> =
        iterable.map { this.save(it) }
                .toList()

    fun findById(id: Long?): T? = db[id]

    fun existsById(id: Long): Boolean = db.containsKey(id)

    fun findAll(): Iterable<T?> = db.values

    fun findAllById(iterable: Iterable<Long>): Iterable<T?> =
        iterable.filter { db.containsKey(it) }
                .map { db[it] }
                .toList()

    fun count(): Long = db.size.toLong()

    fun deleteById(id: Long) {
        deleteListeners.forEach { l ->
            findById(id)?.let { o -> l.onDelete(o) }
        }
        db.remove(id)
    }

    fun delete(t: T) {
        deleteById(idProvider(t))
    }

    fun deleteAll(iterable: Iterable<T>) {
        iterable.filter { i -> db.containsKey(idProvider(i)) }
                .forEach { i -> deleteById(idProvider(i)) }
    }

    fun deleteAll() {
        db.values.forEach { obj ->
            obj?.let { deleteById(idProvider(obj)) }
        }
    }

    fun addDeleteListener(deleteListener: DeleteListener<T>) {
        if (!deleteListeners.contains(deleteListener)) {
            deleteListeners.add(deleteListener)
        }
    }

    fun removeDeleteListener(deleteListener: DeleteListener<T>) {
        deleteListeners.remove(deleteListener)
    }

    fun addSaveListener(saveListener: SaveListener<T>) {
        if (!saveListeners.contains(saveListener)) {
            saveListeners.add(saveListener)
        }
    }

    fun removeSaveListener(saveListener: SaveListener<T>) {
        saveListeners.remove(saveListener)
    }
}
