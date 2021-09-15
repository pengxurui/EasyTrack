package com.pengxr.easytrack.core

/**
 * Created by pengxr on 10/9/2021
 */
interface ITrackNode : ITrackModel {

    val parent: ITrackNode?
}