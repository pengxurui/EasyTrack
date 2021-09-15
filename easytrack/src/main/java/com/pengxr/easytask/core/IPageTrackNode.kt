package com.pengxr.easytask.core

/**
 * Created by pengxr on 10/9/2021
 */
interface IPageTrackNode : ITrackModel {

    fun referrerKeyMap(): Map<String, String>?

    fun referrerTrackNode(): ITrackNode?
}