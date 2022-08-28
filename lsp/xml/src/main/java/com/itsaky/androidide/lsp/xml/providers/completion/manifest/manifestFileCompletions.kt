/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.xml.providers.completion.manifest

import com.android.SdkConstants.ANDROID_MANIFEST_XML
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.VMUtils

const val MANIFEST_TAG_PREFIX = "AndroidManifest"

fun canCompleteManifest(pathData: ResourcePathData, type: NodeType): Boolean {
  return pathData.file.name == ANDROID_MANIFEST_XML ||
    (VMUtils.isJvm() &&
      pathData.file.name.startsWith("Manifest") &&
      pathData.file.name.endsWith("_template.xml"))
}

/**
 * Transforms entry name to tag name.
 *
 * For example: `AndroidManifestUsesPermission` -> `uses-permission`
 */
fun transformToTagName(entryName: String): String {
  val name = StringBuilder()
  var index = MANIFEST_TAG_PREFIX.length
  while (index < entryName.length) {
    var c = entryName[index]
    if (c.isUpperCase()) {
      if (index != MANIFEST_TAG_PREFIX.length) {
        name.append('-')
      }
      c = c.lowercaseChar()
    }

    name.append(c)
    ++index
  }
  return name.toString()
}

/**
 * Transforms entry name to tag name.
 *
 * For example: `uses-permission` -> `AndroidManifestUsesPermission`
 */
fun transformToEntryName(tagName: String): String {
  if (tagName == "manifest") {
    return MANIFEST_TAG_PREFIX
  }

  val name = StringBuilder(MANIFEST_TAG_PREFIX)

  var index = 0
  var capitalize = false
  while (index < tagName.length) {
    var c = tagName[index]
    if (c == '-') {
      capitalize = true
      ++index
      continue
    }
    if (index == 0 || capitalize) {
      c = c.uppercaseChar()
      capitalize = false
    }
    name.append(c)
    ++index
  }

  return name.toString()
}