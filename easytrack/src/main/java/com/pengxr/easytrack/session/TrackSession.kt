package com.pengxr.easytrack.session

import com.pengxr.easytrack.core.TrackParams

/**
 * 埋点会话
 * <p>
 * Created by pengxr on 21/8/2021
 */
class TrackSession internal constructor() : TrackParams() {

    var isEnabled = true

//    private val map = HashMap<Class<ITrackModel>, ITrackModel?>()
//
//    operator fun set(clazz: Class<ITrackModel>, model: ITrackModel?) {
//        map[clazz] = model
//    }
//
//    operator fun get(clazz: Class<ITrackModel>): ITrackModel? = map[clazz]
}