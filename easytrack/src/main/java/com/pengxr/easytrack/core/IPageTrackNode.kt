package com.pengxr.easytrack.core

/**
 * Created by pengxr on 10/9/2021
 */
interface IPageTrackNode : ITrackModel {

    fun referrerKeyMap(): Map<String, String>?

    fun referrerSnapshot(): ITrackNode?
}