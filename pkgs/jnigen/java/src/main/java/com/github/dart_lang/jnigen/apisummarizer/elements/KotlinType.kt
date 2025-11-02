// Copyright (c) 2023, the Dart project authors. Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.
package com.github.dart_lang.jnigen.apisummarizer.elements

import java.util.stream.Collectors
import kotlin.metadata.KmClassifier
import kotlin.metadata.KmType
import kotlin.metadata.KmTypeProjection
import kotlin.metadata.isNullable

class KotlinType {
    var kind: String? = null
    var name: String? = null
    var id: Int = 0
    var arguments: MutableList<KotlinTypeProjection?>? = null
    var isNullable: Boolean = false

    companion object {
        @JvmStatic
        fun fromKmType(t: KmType?): KotlinType? {
            if (t == null) return null
            val type = KotlinType()
            // Processing the information needed from the flags.
            type.isNullable = t.isNullable
            val classifier: KmClassifier = t.classifier
            if (classifier is KmClassifier.Class) {
                type.kind = "class"
                type.name = classifier.name
            } else if (classifier is KmClassifier.TypeAlias) {
                type.kind = "typeAlias"
                type.name = classifier.name
            } else if (classifier is KmClassifier.TypeParameter) {
                type.kind = "typeParameter"
                type.id = classifier.id
            }
            type.arguments = t.arguments.stream()
                .map { t: KmTypeProjection? ->
                    KotlinTypeProjection.fromKmTypeProjection(
                        t
                    )
                }
                .collect(Collectors.toList())
            return type
        }
    }
}
