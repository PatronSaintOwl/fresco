/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.common.media

import androidx.annotation.NonNull
import com.facebook.common.internal.ImmutableMap
import java.util.Locale

/** Utility class. */
object MediaUtils {

  @JvmField // Additional mime types that we know to be a particular media type but which may not be
  // supported natively on the device.
  val ADDITIONAL_ALLOWED_MIME_TYPES =
      ImmutableMap.of("mkv", "video/x-matroska", "glb", "model/gltf-binary")

  @JvmStatic
  fun isPhoto(mimeType: String?): Boolean {
    return mimeType != null && mimeType.startsWith("image/")
  }

  @JvmStatic
  fun isVideo(mimeType: String?): Boolean {
    return mimeType != null && mimeType.startsWith("video/")
  }

  @JvmStatic
  fun isThreeD(mimeType: String?): Boolean {
    return mimeType != null && mimeType == "model/gltf-binary"
  }

  @JvmStatic
  fun extractMime(@NonNull path: String): String? {
    var extension = extractExtension(path)
    if (extension == null) {
      return null
    }
    extension = extension.toLowerCase(Locale.US)
    var mimeType = MimeTypeMapWrapper.getMimeTypeFromExtension(extension)

    // If we did not find a mime type for the extension specified, check our additional
    // extension/mime-type mappings.
    if (mimeType == null) {
      mimeType = ADDITIONAL_ALLOWED_MIME_TYPES[extension]
    }
    return mimeType
  }

  private fun extractExtension(@NonNull path: String): String? {
    val pos = path.lastIndexOf('.')
    return if (pos < 0 || pos == path.length - 1) {
      null
    } else path.substring(pos + 1)
  }

  /**
   * @return true if the mime type is one of our whitelisted mimetypes that we support beyond what
   * the native platform supports.
   */
  @JvmStatic
  fun isNonNativeSupportedMimeType(@NonNull mimeType: String): Boolean {
    return ADDITIONAL_ALLOWED_MIME_TYPES.containsValue(mimeType)
  }
}