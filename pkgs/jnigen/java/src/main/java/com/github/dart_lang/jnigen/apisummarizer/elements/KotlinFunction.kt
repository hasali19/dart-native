// Copyright (c) 2023, the Dart project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.github.dart_lang.jnigen.apisummarizer.elements

import java.util.stream.Collectors
import kotlin.metadata.KmFunction
import kotlin.metadata.KmTypeParameter
import kotlin.metadata.KmValueParameter
import kotlin.metadata.Visibility
import kotlin.metadata.isOperator
import kotlin.metadata.isSuspend
import kotlin.metadata.jvm.JvmMethodSignature
import kotlin.metadata.jvm.signature
import kotlin.metadata.visibility

class KotlinFunction {
    /** Name in the byte code.  */
    var name: String? = null

    var descriptor: String? = null

    /** Name in the Kotlin's metadata.  */
    var kotlinName: String? = null

    var valueParameters: MutableList<KotlinValueParameter?>? = null
    var returnType: KotlinType? = null
    var receiverParameterType: KotlinType? = null
    var contextReceiverTypes: MutableList<KotlinType?>? = null
    var typeParameters: MutableList<KotlinTypeParameter?>? = null
    var isSuspend: Boolean = false
    var isOperator: Boolean = false
    var isPublic: Boolean = false
    var isPrivate: Boolean = false
    var isProtected: Boolean = false
    var isInternal: Boolean = false

    companion object {
        @JvmStatic
        fun fromKmFunction(f: KmFunction): KotlinFunction {
            val `fun` = KotlinFunction()
            val signature: JvmMethodSignature? = f.signature
            `fun`.descriptor = if (signature == null) null else signature.descriptor
            `fun`.name = if (signature == null) null else signature.name
            `fun`.kotlinName = f.name
            // Processing the information needed from the flags.
            `fun`.isSuspend = f.isSuspend
            `fun`.isOperator = f.isOperator
            `fun`.valueParameters = f.valueParameters.stream()
                .map<KotlinValueParameter?> { p: KmValueParameter? ->
                    KotlinValueParameter.fromKmValueParameter(
                        p
                    )
                }
                .collect(Collectors.toList())
            `fun`.returnType = KotlinType.fromKmType(f.returnType)
            `fun`.receiverParameterType = KotlinType.fromKmType(f.receiverParameterType)
            // TODO:
            // `fun`.contextReceiverTypes = f.contextReceiverTypes.stream()
            //     .map<KotlinType?> { t: KmType? -> KotlinType.fromKmType(t) }
            //     .collect(Collectors.toList())
            `fun`.typeParameters = f.typeParameters.stream()
                .map<KotlinTypeParameter?> { t: KmTypeParameter? ->
                    KotlinTypeParameter.fromKmTypeParameter(
                        t
                    )
                }
                .collect(Collectors.toList())
            `fun`.isPublic = f.visibility == Visibility.PUBLIC
            `fun`.isPrivate = f.visibility == Visibility.PRIVATE
            `fun`.isProtected = f.visibility == Visibility.PROTECTED
            `fun`.isInternal = f.visibility == Visibility.INTERNAL
            return `fun`
        }
    }
}
