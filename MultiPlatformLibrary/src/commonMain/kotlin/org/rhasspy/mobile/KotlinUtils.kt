package org.rhasspy.mobile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

//https://stackoverflow.com/questions/67179257/how-can-i-convert-an-int-to-a-bytearray-and-then-convert-it-back-to-an-int-with
fun Number.toByteArray(size: Int = 4): ByteArray =
    ByteArray(size) { i -> (this.toLong() shr (i * 8)).toByte() }

fun ByteArray.addWavHeader(): ByteArray {
    val wavHeader = this.size.toLong().getWavHeaderForSize()
    return wavHeader + this
}

//https://github.com/razzo04/rhasspy-mobile-app/blob/3c59971270eab0278cd5dbf6adac4064b5f14908/android/app/src/main/java/com/example/rhasspy_mobile_app/WakeWordService.java#L151
fun Long.getWavHeaderForSize(): ByteArray {
    val dataSize = (this + 44 - 8).toByteArray()
    val audioDataSize = this.toByteArray()
    val wavHeader = arrayOf(
        82,
        73,
        70,
        70,
        dataSize[0],
        dataSize[1],
        dataSize[2],
        dataSize[3], //4-7 overall size
        87,
        65,
        86,
        69,
        102,
        109,
        116,
        32,
        16,
        0,
        0,
        0,
        1,
        0,
        1,
        0,
        -128,
        62,
        0,
        0,
        0,
        125,
        0,
        0,
        2,
        0,
        16,
        0,
        100,
        97,
        116,
        97,
        audioDataSize[0],
        audioDataSize[1],
        audioDataSize[2],
        audioDataSize[3] //40-43 data size of rest
    )
    return wavHeader.toByteArray()
}

fun <T1, T2, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    transform: (T1, T2) -> R
): StateFlow<R> = combine(flow1, flow2) { o1, o2 ->
    transform.invoke(o1, o2)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value))

fun <T1, T2, T3, T4, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    transform: (T1, T2, T3, T4) -> R
): StateFlow<R> = combine(flow1, flow2, flow3, flow4) { o1, o2, o3, o4 ->
    transform.invoke(o1, o2, o3, o4)
}.stateIn(
    scope,
    sharingStarted,
    transform.invoke(flow1.value, flow2.value, flow3.value, flow4.value)
)

inline fun <reified T, R> combineStateFlow(
    vararg flows: StateFlow<T>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    crossinline transform: (Array<T>) -> R
): StateFlow<R> = combine(flows = flows) {
    transform.invoke(it)
}.stateIn(
    scope = scope,
    started = sharingStarted,
    initialValue = transform.invoke(flows.map {
        it.value
    }.toTypedArray())
)

fun <T1, T2, T3, R> combineState(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    transform: (T1, T2, T3) -> R
): StateFlow<R> = combine(flow1, flow2, flow3) { o1, o2, o3 ->
    transform.invoke(o1, o2, o3)
}.stateIn(scope, sharingStarted, transform.invoke(flow1.value, flow2.value, flow3.value))

fun <T1, T2> combineStateNotEquals(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily
): StateFlow<Boolean> = combine(flow1, flow2) { o1, o2 ->
    o1 != o2
}.stateIn(scope, sharingStarted, flow1.value != flow2.value)

fun combineAny(
    vararg flows: StateFlow<Boolean>,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
): StateFlow<Boolean> = combine(*flows) { array: Array<Boolean> ->
    array.contains(true)
}.stateIn(scope, sharingStarted, flows.find { it.value } != null)

fun <T, R> StateFlow<T>.mapReadonlyState(
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    sharingStarted: SharingStarted = SharingStarted.Lazily,
    transform: (T) -> R
): StateFlow<R> = this.map {
    transform(it)
}.stateIn(scope, sharingStarted, transform.invoke(this.value))

val <T> MutableStateFlow<T>.readOnly get(): StateFlow<T> = this

val <T> MutableSharedFlow<T>.readOnly get(): Flow<T> = this

val <T> MutableList<T>.readOnly get(): List<T> = this

inline fun <T1 : Any, T2 : Any> notNull(
    p1: T1?,
    p2: T2?,
    block: (T1, T2) -> Unit,
    run: () -> Unit
) {
    return if (p1 != null && p2 != null) block(p1, p2) else run()
}