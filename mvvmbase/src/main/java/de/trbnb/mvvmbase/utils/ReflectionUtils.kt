package de.trbnb.mvvmbase.utils

import androidx.databinding.Observable
import androidx.lifecycle.LifecycleOwner
import de.trbnb.mvvmbase.BR
import de.trbnb.mvvmbase.MvvmBase
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

/**
 * Searches for a given parameterized class type in the receivers type hierachy and returns it if it was found.
 * Returns `null` otherwise.
 */
inline fun <reified T> Any.findGenericSuperclass(): ParameterizedType? {
    return javaClass.findGenericSuperclass(T::class.java)
}

/**
 * Searches for a given parameterized class type in the receivers hierachy and returns it if it was found.
 * Returns `null` otherwise.
 */
tailrec fun <T> Type.findGenericSuperclass(targetType: Class<T>): ParameterizedType? {
    val genericSuperClass = ((this as? Class<*>)?.genericSuperclass) ?: return null

    if ((genericSuperClass as? ParameterizedType)?.rawType == targetType) {
        return genericSuperClass
    }

    return genericSuperClass.findGenericSuperclass(targetType)
}

/**
 * Finds the field ID of the given property.
 *
 * @see MvvmBase.init
 */
fun KProperty<*>.resolveFieldId(): Int = MvvmBase.lookupFieldIdByName(brFieldName()) ?: BR._all

/**
 * Converts a property name to a field name like the data binding compiler.
 *
 * See also:
 * https://android.googlesource.com/platform/frameworks/data-binding/+/master/compiler/src/main/java/android/databinding/annotationprocessor/ProcessBindable.java#216
 */
@Suppress("MagicNumber")
internal fun KProperty<*>.brFieldName(): String {
    val isBoolean = returnType.classifier == Boolean::class && !returnType.isMarkedNullable
    if (name.startsWith("is") && Character.isJavaIdentifierStart(name[2]) && isBoolean) {
        return name[2].lowercaseChar() + name.substring(3)
    }

    return name
}

/**
 * Invokes [action] everytime notifyPropertyChanged is called for the receiver property.
 */
internal inline fun <T> KProperty0<T>.observeBindable(
    observable: Observable,
    lifecycleOwner: LifecycleOwner,
    invokeImmediately: Boolean = true,
    crossinline action: (T) -> Unit
) {
    val propertyId = resolveFieldId().takeUnless { it == BR._all } ?: throw IllegalArgumentException("Property isn't bindable")
    observable.addOnPropertyChangedCallback(lifecycleOwner, object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, changedPropertyId: Int) {
            if (changedPropertyId == propertyId) {
                action(get())
            }
        }
    })

    if (invokeImmediately) {
        action(get())
    }
}
