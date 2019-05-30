package pl.draciel.octocat.core.utility

/**
 * Interface to help clearing resources in structured way.
 */
interface Clearable {
    /**
     * Clears/Releases allocated resources.
     */
    fun clear()
}
