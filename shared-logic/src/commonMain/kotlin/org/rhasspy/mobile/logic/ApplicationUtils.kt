package org.rhasspy.mobile.logic

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.rhasspy.mobile.logic.nativeutils.NativeApplication


fun KoinComponent.openLink(url: String) = this.get<NativeApplication>().openLink(url)